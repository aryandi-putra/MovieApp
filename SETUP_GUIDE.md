# Setup Guide

## 🚀 Quick Start Guide

### Step 1: Get TMDB API Key

1. Go to [The Movie Database (TMDB)](https://www.themoviedb.org/)
2. Create a free account
3. Go to Settings → API
4. Request an API key (choose "Developer" option)
5. Copy your API key

### Step 2: Add API Key to Project

**Option A: Direct in build.gradle.kts (Quick & Easy)**

Open `app/build.gradle.kts` and replace:

```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"YOUR_API_KEY_HERE\"")
```

With your actual key:

```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"abc123xyz789...\"")
```

**Option B: Using local.properties (Recommended)**

1. Open/Create `local.properties` file in project root
2. Add:

```properties
TMDB_API_KEY=your_api_key_here
```

3. Update `app/build.gradle.kts`:

```kotlin
// Add at the top level
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

android {
    // ... existing config
    
    defaultConfig {
        // Replace existing buildConfigField with:
        val apiKey = localProperties.getProperty("TMDB_API_KEY") ?: ""
        buildConfigField("String", "TMDB_API_KEY", "\"$apiKey\"")
    }
}
```

### Step 3: Sync Gradle

```bash
./gradlew build
```

Or in Android Studio: **File → Sync Project with Gradle Files**

### Step 4: Run the App

```bash
./gradlew installDebug
```

Or click the **Run** button in Android Studio.

---

## 🛠️ Build Commands

### Clean Build

```bash
./gradlew clean build
```

### Run Tests

```bash
./gradlew test
```

### Run Specific Module Tests

```bash
./gradlew :domain:test
./gradlew :data:test
./gradlew :app:test
```

### Install Debug APK

```bash
./gradlew installDebug
```

### Generate APK

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

---

## 📦 Module Dependencies

If you see dependency errors, verify module dependencies:

```kotlin
// app/build.gradle.kts
dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core"))
}

// data/build.gradle.kts
dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))
}

// core/build.gradle.kts
dependencies {
    // No project dependencies
}

// domain/build.gradle.kts
dependencies {
    // No project dependencies
}
```

---

## 🔧 Troubleshooting

### Issue: "Unresolved reference: BuildConfig"

**Solution**: Sync Gradle or rebuild:

```bash
./gradlew clean build
```

Make sure `buildFeatures { buildConfig = true }` is in `app/build.gradle.kts`

### Issue: "Unresolved reference: dagger/hilt"

**Solution**: Make sure you have the Hilt plugin:

```kotlin
// app/build.gradle.kts
plugins {
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}
```

### Issue: API key not working

**Symptoms**: 401 Unauthorized error

**Solution**:

1. Verify your API key is correct
2. Make sure there are no extra spaces
3. Check your TMDB account is active
4. Wait a few minutes (API key activation delay)

### Issue: Gradle sync failed

**Solution**:

1. **Invalidate Caches**: File → Invalidate Caches → Invalidate and Restart
2. Delete `.gradle` folder in project root
3. Delete `.idea` folder in project root
4. Sync again

### Issue: "Cannot access class"

**Solution**: Make sure all modules are properly included in `settings.gradle.kts`:

```kotlin
include(":app")
include(":domain")
include(":data")
include(":core")
```

---

## 📱 Running on Device/Emulator

### Physical Device

1. Enable Developer Options on your phone
2. Enable USB Debugging
3. Connect via USB
4. Accept USB debugging prompt on phone
5. Run the app from Android Studio

### Emulator

1. **Create AVD**: Tools → Device Manager → Create Device
2. Choose device (e.g., Pixel 6)
3. Choose system image (e.g., API 34)
4. Finish and launch emulator
5. Run the app

---

## 🧪 Testing Setup

### Unit Tests (Domain Layer)

```bash
./gradlew :domain:test
```

Tests are in: `domain/src/test/java/`

### Unit Tests (Data Layer)

```bash
./gradlew :data:test
```

Tests are in: `data/src/test/java/`

### Unit Tests (App Layer)

```bash
./gradlew :app:test
```

Tests are in: `app/src/test/java/`

### Instrumentation Tests (UI Tests)

```bash
./gradlew connectedAndroidTest
```

Tests are in: `app/src/androidTest/java/`

---

## 🎨 Adding New Features

### Step 1: Define Domain Model

```kotlin
// domain/src/main/java/com/aryandi/movieapp/domain/model/
data class NewFeature(...)
```

### Step 2: Create Use Case

```kotlin
// domain/src/main/java/com/aryandi/movieapp/domain/usecase/
class NewFeatureUseCase(...)
```

### Step 3: Update Repository Interface

```kotlin
// domain/src/main/java/com/aryandi/movieapp/domain/repository/
interface MovieRepository {
    fun getNewFeature(): Flow<Result<NewFeature>>
}
```

### Step 4: Implement in Data Layer

```kotlin
// data/src/main/java/com/aryandi/movieapp/data/repository/
class MovieRepositoryImpl : MovieRepository {
    override fun getNewFeature() = flow { ... }
}
```

### Step 5: Create ViewModel

```kotlin
// app/src/main/java/com/aryandi/movieapp/presentation/newfeature/
@HiltViewModel
class NewFeatureViewModel @Inject constructor(...)
```

### Step 6: Create UI

```kotlin
// app/src/main/java/com/aryandi/movieapp/presentation/newfeature/
@Composable
fun NewFeatureScreen(...)
```

### Step 7: Add to Navigation

```kotlin
// app/src/main/java/com/aryandi/movieapp/MainActivity.kt
composable("newfeature") {
    NewFeatureScreen()
}
```

---

## 📚 Next Steps

After setup, you can:

1. **Explore the code structure** - See how layers interact
2. **Add a new feature** - Following the architecture
3. **Write tests** - For domain, data, and presentation layers
4. **Customize UI** - Modify theme, colors, components
5. **Add more endpoints** - Extend the API service
6. **Implement caching** - Add Room database
7. **Add navigation** - Create more screens

---

## 🤝 Need Help?

- Check [README.md](README.md) for architecture overview
- Check [ARCHITECTURE.md](ARCHITECTURE.md) for detailed explanation
- Open an issue on GitHub
-
Review [Eran Boudjnah's Clean Architecture repo](https://github.com/EranBoudjnah/CleanArchitectureForAndroid)

---

**Happy Coding! 🎉**
