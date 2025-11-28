# Gradle Fix Guide

## ✅ Fixes Applied

I've fixed several Gradle configuration issues:

### 1. **AGP Version**

- Updated to `8.9.1` (latest stable version)
- Updated Gradle wrapper to `8.15` for compatibility

### 2. **Domain Module (Pure Kotlin)**

- Fixed to use direct Kotlin JVM plugin
- Removed version catalog references for better compatibility
- Added explicit dependencies

### 3. **Module Dependencies**

- Removed incorrect `core` dependency from `data` module
- Data module should only depend on `domain`

### 4. **Android Manifests**

- Added `AndroidManifest.xml` for `data` and `core` modules
- Required for Android library modules

### 5. **Root Build File**

- Added all necessary plugin declarations

### 6. **Compose Compatibility** ⭐ IMPORTANT

- **With Kotlin 2.0+, use the `kotlin-compose` plugin** instead of manual compiler version
- Added `kotlin-compose` plugin to `core` and `app` modules
- Removed manual `composeOptions.kotlinCompilerExtensionVersion`
- The plugin automatically handles Compose compiler compatibility with Kotlin version

## 🔧 Steps to Fix in Android Studio

### Step 1: Sync Gradle

```
File → Sync Project with Gradle Files
```

### Step 2: If Sync Fails, Clean & Rebuild

```
Build → Clean Project
Build → Rebuild Project
```

### Step 3: Invalidate Caches (if still failing)

```
File → Invalidate Caches → Invalidate and Restart
```

### Step 4: Check JDK Version

```
File → Project Structure → SDK Location
- Ensure JDK 11 or higher is selected
- Gradle JDK should be 11 or 17
```

## 🚨 Common Gradle Errors & Solutions

### Error: "Unresolved reference: BuildConfig"

**Solution:**

1. Make sure `buildFeatures { buildConfig = true }` is in `app/build.gradle.kts`
2. Sync Gradle
3. Rebuild project

### Error: "Plugin [id: 'kotlin-kapt'] was not found"

**Solution:**

- Already fixed in the files. Just sync Gradle.

### Error: "Cannot access class 'X'"

**Solution:**

1. Check that modules are properly included in `settings.gradle.kts`
2. Sync Gradle
3. If persists, restart Android Studio

### Error: "Compose compiler version mismatch"

**Solution:**

- **Use `kotlin-compose` plugin** instead of manual version (Kotlin 2.0+ requirement)
- Remove `composeOptions { kotlinCompilerExtensionVersion = "..." }`
- Add to plugins block: `alias(libs.plugins.kotlin.compose)`
- Already fixed in the project
- Sync Gradle

**Why?** Kotlin 2.0+ has the Compose Compiler built-in. The plugin automatically selects the correct
version.

### Error: "Duplicate class found"

**Solution:**

1. Clean project: `Build → Clean Project`
2. Delete `.gradle` and `build` folders
3. Sync Gradle

## 📋 Verification Checklist

After syncing, verify:

- [ ] No Gradle sync errors
- [ ] All 4 modules visible in Project Structure
- [ ] Dependencies resolved (no red underlines)
- [ ] Can build successfully: `Build → Make Project`

## 🛠️ Terminal Commands (Alternative)

If Android Studio has issues, try from terminal:

```bash
# Clean
./gradlew clean

# Build all modules
./gradlew build

# Or build specific module
./gradlew :app:build
./gradlew :domain:build
./gradlew :data:build
./gradlew :core:build
```

## 📦 Module Dependencies Summary

```
app
 ├── depends on → domain
 ├── depends on → data
 └── depends on → core

data
 └── depends on → domain

core
 └── (no dependencies)

domain
 └── (no dependencies)
```

## 🎯 Expected Gradle Output

After successful sync, you should see:

```
BUILD SUCCESSFUL in Xs
4 modules
```

## 🔍 Troubleshooting Specific Errors

### If you see: "Unable to find method"

1. Check Gradle version: `./gradlew --version`
2. Should be 8.13 or higher
3. Update if needed in `gradle/wrapper/gradle-wrapper.properties`

### If you see: "Namespace not specified"

- All modules already have `namespace` defined in their `build.gradle.kts`

### If you see: "compileSdk is not specified"

- All modules have `compileSdk = 36` set

### If you see: "Kotlin version conflict"

- All modules use Kotlin 2.0.21
- Check that no manual Kotlin dependencies have different versions

## 📝 Files Modified

1. `gradle/libs.versions.toml` - AGP version
2. `domain/build.gradle.kts` - Kotlin JVM setup
3. `data/build.gradle.kts` - Removed core dependency
4. `core/build.gradle.kts` - Added kotlin-compose plugin, removed manual compiler version
5. `build.gradle.kts` - Plugin declarations
6. `data/src/main/AndroidManifest.xml` - Created
7. `core/src/main/AndroidManifest.xml` - Created

## ✨ After Successful Sync

Once Gradle syncs successfully:

1. **Add your TMDB API key** to `app/build.gradle.kts`
2. **Sync again**
3. **Run the app**

Replace in `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"YOUR_API_KEY_HERE\"")
```

With:

```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"your_actual_api_key\"")
```

## 🎉 Success Indicators

You'll know it worked when:

- ✅ Gradle sync completes without errors
- ✅ No red underlines in code
- ✅ Can navigate to classes using Cmd+Click (Mac) or Ctrl+Click (Windows)
- ✅ `Build → Make Project` succeeds
- ✅ Can run the app

## 📚 Need More Help?

If you still have issues:

1. Check Android Studio version (should be Hedgehog or newer)
2. Check JDK version (should be 11 or 17)
3. Try creating a new project and comparing settings
4. Check the error log: `View → Tool Windows → Build`

## 🔗 Reference Links

- [Gradle Documentation](https://docs.gradle.org/)
- [Android Gradle Plugin](https://developer.android.com/build)
- [Kotlin Gradle Plugin](https://kotlinlang.org/docs/gradle.html)
- [Hilt Setup](https://dagger.dev/hilt/gradle-setup.html)

---

**Good luck! The project should sync successfully now.** 🚀
