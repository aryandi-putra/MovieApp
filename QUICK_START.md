# ⚡ Quick Start Guide

## 🚀 Get Running in 5 Minutes

### 1️⃣ Open Project in Android Studio

```
File → Open → Select MovieApp folder
```

### 2️⃣ Sync Gradle

```
Wait for automatic sync or:
File → Sync Project with Gradle Files
```

### 3️⃣ Get TMDB API Key

1. Go to https://www.themoviedb.org/
2. Sign up (free)
3. Go to Settings → API
4. Request API Key (Developer option)
5. Copy your API key

### 4️⃣ Add API Key

Open `app/build.gradle.kts` and replace:

```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"YOUR_API_KEY_HERE\"")
```

With your key:

```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"abc123your_actual_key\"")
```

### 5️⃣ Sync Again

```
File → Sync Project with Gradle Files
```

### 6️⃣ Run the App

```
Click the green ▶️ Run button
or
Run → Run 'app'
```

---

## 🎯 What You'll See

The app will display popular movies from TMDB in a grid layout with:

- Movie posters
- Movie titles
- Star ratings

---

## 🐛 Troubleshooting

### Problem: Gradle sync fails

**Solution:** See [GRADLE_FIX.md](GRADLE_FIX.md)

### Problem: API calls return 401 error

**Solution:** Check your API key is correct

### Problem: No movies showing

**Solutions:**

1. Check internet connection
2. Verify API key
3. Check Logcat for errors

---

## 📚 Next Steps

After running the app:

1. **Explore the code** - Start with `MoviesScreen.kt`
2. **Read the docs** - Check [INDEX.md](INDEX.md) for guide
3. **Add a feature** - Try adding movie details screen
4. **Write tests** - Add unit tests for use cases

---

## 🎓 Learning Path

### Day 1: Setup & Explore (Today!)

- ✅ Run the app
- ✅ Browse the code
- ✅ Read README.md

### Day 2: Understand Architecture

- 📖 Read ARCHITECTURE.md
- 📊 Study DIAGRAMS.md
- 🗺️ Review FILE_STRUCTURE.md

### Day 3: Hands-on Coding

- 🔨 Add movie details screen
- 🧪 Write your first test
- 📝 Use CHEATSHEET.md

### Week 2: Advanced Features

- 💾 Add Room database (caching)
- 🔍 Implement search
- ⭐ Add favorites

---

## 💡 Pro Tips

1. **Keep CHEATSHEET.md open** while coding
2. **Follow the layer boundaries** - don't bypass
3. **Write tests** - especially for use cases
4. **Use Git** - commit often
5. **Ask questions** - refer to documentation

---

## 🎉 You're Ready!

The project is now set up with:

- ✅ Clean Architecture
- ✅ 4 Gradle modules
- ✅ MVVM + Jetpack Compose
- ✅ Hilt DI
- ✅ Retrofit
- ✅ Comprehensive documentation

**Happy Coding!** 🚀

---

## 📞 Need Help?

- 📖 Check [INDEX.md](INDEX.md) for documentation index
- 🔧 See [GRADLE_FIX.md](GRADLE_FIX.md) for build issues
- 📚 Read [SETUP_GUIDE.md](SETUP_GUIDE.md) for detailed setup
- 🏛️ Study [ARCHITECTURE.md](ARCHITECTURE.md) for concepts
