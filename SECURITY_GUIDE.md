# 🔒 Security Guide - Protecting Your API Keys

## ⚠️ **CRITICAL: Your API Key is Currently Exposed!**

Your TMDB API key is currently hardcoded in `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"845a0f79064a3c8f0125a389a7ed651c\"")
```

**This is visible in your Git history and should NOT be committed to public repositories!**

---

## 🛡️ How to Secure Your API Key

### Method 1: Use `local.properties` (Recommended)

#### Step 1: Add API Key to `local.properties`

Open or create `local.properties` (in project root):

```properties
# local.properties
sdk.dir=/path/to/your/Android/sdk

# Add your API key here
TMDB_API_KEY=845a0f79064a3c8f0125a389a7ed651c
```

#### Step 2: Read from `local.properties` in `app/build.gradle.kts`

Replace the current hardcoded key with:

```kotlin
// app/build.gradle.kts

// Load local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { 
        localProperties.load(it) 
    }
}

android {
    namespace = "com.aryandi.movieapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.aryandi.movieapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Read API key from local.properties
        val tmdbApiKey = localProperties.getProperty("TMDB_API_KEY") ?: ""
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
    }
    
    // ... rest of config
}
```

#### Step 3: Verify `local.properties` is in `.gitignore`

✅ Already added! Check `.gitignore`:

```
# Local configuration file (sdk path, etc)
local.properties
```

---

### Method 2: Use `secrets.properties` (Alternative)

If you want to separate secrets from SDK path:

#### Step 1: Create `secrets.properties`

```properties
# secrets.properties (in project root)
TMDB_API_KEY=845a0f79064a3c8f0125a389a7ed651c
```

#### Step 2: Update `app/build.gradle.kts`

```kotlin
// Load secrets.properties
val secretsProperties = Properties()
val secretsPropertiesFile = rootProject.file("secrets.properties")
if (secretsPropertiesFile.exists()) {
    secretsPropertiesFile.inputStream().use { 
        secretsProperties.load(it) 
    }
}

android {
    defaultConfig {
        // Read API key from secrets.properties
        val tmdbApiKey = secretsProperties.getProperty("TMDB_API_KEY") ?: ""
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
    }
}
```

#### Step 3: Add to `.gitignore`

✅ Already added! Check `.gitignore`:

```
secrets.properties
```

---

### Method 3: Environment Variables (CI/CD)

For production/CI builds:

#### Step 1: Set Environment Variable

```bash
# Linux/Mac
export TMDB_API_KEY="845a0f79064a3c8f0125a389a7ed651c"

# Windows
set TMDB_API_KEY=845a0f79064a3c8f0125a389a7ed651c
```

#### Step 2: Read in `app/build.gradle.kts`

```kotlin
android {
    defaultConfig {
        // Try environment variable first, then local.properties
        val tmdbApiKey = System.getenv("TMDB_API_KEY") 
            ?: localProperties.getProperty("TMDB_API_KEY") 
            ?: ""
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
    }
}
```

---

## 🚨 What's Protected by `.gitignore`

### ✅ Files That Won't Be Committed:

```
# Secrets & API Keys
local.properties          ✅
secrets.properties        ✅
apikey.properties        ✅
config.properties        ✅
credentials.properties   ✅

# Keystore files
*.jks                    ✅
*.keystore               ✅
keystore.properties      ✅

# Google Services
google-services.json     ✅

# Build outputs
build/                   ✅
*.apk                    ✅
*.aab                    ✅

# IDE files
.idea/ (most files)      ✅
*.iml                    ✅

# OS files
.DS_Store                ✅
Thumbs.db                ✅
```

### ⚠️ Files You SHOULD Commit:

```
✅ Source code (.kt, .java)
✅ Gradle files (build.gradle.kts, settings.gradle.kts)
✅ Gradle wrapper (gradlew, gradle-wrapper.jar)
✅ Resource files (layouts, drawables, strings)
✅ AndroidManifest.xml
✅ README.md and documentation
✅ .gitignore itself
```

---

## 📋 Security Checklist

Before committing to Git:

- [ ] Remove hardcoded API key from `app/build.gradle.kts`
- [ ] Add API key to `local.properties`
- [ ] Verify `local.properties` is in `.gitignore`
- [ ] Test build works with new setup
- [ ] Check `git status` - ensure no secrets are staged
- [ ] Review files with `git diff` before committing
- [ ] If API key was already committed, see "Damage Control" below

---

## 🔥 Damage Control: If You Already Committed API Key

If you've already committed your API key to Git:

### Option 1: If Not Pushed Yet

```bash
# Amend the last commit
git add app/build.gradle.kts
git commit --amend --no-edit

# Or reset to before the commit
git reset HEAD~1
```

### Option 2: If Already Pushed (More Complex)

1. **Revoke the exposed API key immediately!**
    - Go to TMDB → Settings → API
    - Delete the old API key
    - Generate a new one

2. **Remove from Git history** (if needed):

```bash
# WARNING: This rewrites history!
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch app/build.gradle.kts" \
  --prune-empty --tag-name-filter cat -- --all
```

3. **Force push** (only if you control the repository):

```bash
git push origin --force --all
```

4. **Update with new key** using secure method above

---

## 🎯 Best Practices

### DO ✅

- ✅ Use `local.properties` for local development
- ✅ Use environment variables for CI/CD
- ✅ Keep `.gitignore` updated
- ✅ Review files before committing
- ✅ Use separate keys for dev/staging/prod
- ✅ Rotate API keys regularly
- ✅ Add secrets to `.gitignore` BEFORE first commit

### DON'T ❌

- ❌ Hardcode API keys in source code
- ❌ Commit `local.properties` or `secrets.properties`
- ❌ Share API keys in screenshots/documentation
- ❌ Use production keys in development
- ❌ Commit keystore files
- ❌ Ignore .gitignore warnings

---

## 🔧 Implementation Steps (Quick Fix)

### 1. Update `app/build.gradle.kts`

Replace the entire file with this secure version:

```kotlin
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

// Load local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

android {
    namespace = "com.aryandi.movieapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.aryandi.movieapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Secure: Read from local.properties
        val tmdbApiKey = localProperties.getProperty("TMDB_API_KEY") ?: ""
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Modules
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Networking (needed for DI module)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

### 2. Update `local.properties`

```properties
sdk.dir=/Users/yourusername/Library/Android/sdk
TMDB_API_KEY=845a0f79064a3c8f0125a389a7ed651c
```

### 3. Test

```bash
./gradlew clean build
```

### 4. Verify

```bash
# This should NOT show local.properties
git status

# This should show .gitignore includes local.properties
cat .gitignore | grep local.properties
```

---

## 📚 Additional Resources

- [Android Secrets in Build Configuration](https://developer.android.com/studio/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code)
- [Best Practices for API Keys](https://cloud.google.com/docs/authentication/api-keys)
- [Git Filter-Branch Guide](https://git-scm.com/docs/git-filter-branch)

---

## ✅ Summary

**Current Status**: 🔴 API key is exposed in `app/build.gradle.kts`

**After Following This Guide**: 🟢 API key secured in `local.properties`

**What's Protected**:

- ✅ API keys
- ✅ Keystore files
- ✅ Build outputs
- ✅ IDE settings
- ✅ OS-specific files

**Remember**: Security is not a one-time task. Always review what you're committing!

---

**⚠️ ACTION REQUIRED**: Move your API key to `local.properties` before pushing to Git!
