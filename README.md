# MovieApp - Clean Architecture Skeleton

A minimal Clean Architecture skeleton for Android, inspired
by [Eran Boudjnah's Clean Architecture for Android](https://github.com/EranBoudjnah/CleanArchitectureForAndroid).

## 🏗️ Architecture Overview

This project follows **Clean Architecture** principles with clear separation of concerns across
multiple layers:

```
┌─────────────────────────────────────────────────────┐
│                    UI LAYER (app)                   │
│         Activities, Composables, ViewModels         │
├─────────────────────────────────────────────────────┤
│              PRESENTATION LAYER (app)               │
│                 ViewModels, UI State                │
├─────────────────────────────────────────────────────┤
│                DOMAIN LAYER (domain)                │
│         Use Cases, Entities, Repository             │
│                   Interfaces                        │
├─────────────────────────────────────────────────────┤
│                 DATA LAYER (data)                   │
│    Repository Impl, DTOs, Mappers, API Service     │
├─────────────────────────────────────────────────────┤
│               DATA SOURCE LAYER (data)              │
│          Retrofit, Room, Remote/Local APIs          │
└─────────────────────────────────────────────────────┘
```

## 📦 Module Structure

The project is organized into 4 Gradle modules:

### 1. **`:domain`** (Pure Kotlin Module)

- **Purpose**: Contains business logic and business rules
- **No Android dependencies** - Pure Kotlin for maximum testability and reusability
- **Contents**:
    - `model/` - Domain entities (e.g., `Movie`)
    - `repository/` - Repository interfaces
    - `usecase/` - Use cases/Interactors (e.g., `GetPopularMoviesUseCase`)
    - `common/` - Common domain classes (e.g., `Result` wrapper)

**Key Benefits:**

- Can be tested with pure JUnit (no Android framework needed)
- Can be reused across platforms (Android, iOS via KMP, Desktop)
- Most stable layer - least likely to change

### 2. **`:data`** (Android Library Module)

- **Purpose**: Implements data operations
- **Depends on**: `:domain`
- **Contents**:
    - `remote/` - API DTOs, Retrofit services
    - `local/` - Room entities and DAOs (when needed)
    - `repository/` - Repository implementations
    - `mapper/` - Converts DTOs ↔ Domain models

**Key Responsibilities:**

- Concrete implementation of domain's repository interfaces
- Data source abstraction (API, Database, Cache)
- Data transformation via Mappers

### 3. **`:core`** (Android Library Module)

- **Purpose**: Shared UI components and utilities
- **No dependencies** on other modules
- **Contents**:
    - `ui/components/` - Reusable Composables
    - Common UI utilities and extensions

### 4. **`:app`** (Android Application Module)

- **Purpose**: Application entry point and UI
- **Depends on**: `:domain`, `:data`, `:core`
- **Contents**:
    - `di/` - Hilt dependency injection modules
    - `presentation/` - ViewModels, UI State, Screens
    - `MainActivity` - Navigation setup

## 🎯 Clean Architecture Principles Applied

### 1. **Dependency Rule**

Dependencies point **inward** (toward domain):

```
app → domain ← data
       ↑
     core
```

- `domain` has **zero dependencies** on other modules
- `data` depends on `domain` (implements its interfaces)
- `app` depends on `domain` and `data`
- `core` is independent (shared utilities)

### 2. **Separation of Concerns**

Each layer has a single responsibility:

| Layer | Responsibility | Example |
|-------|---------------|---------|
| **UI** | Display data, handle user input | `MoviesScreen.kt` |
| **Presentation** | UI logic, state management | `MoviesViewModel` |
| **Domain** | Business rules | `GetPopularMoviesUseCase` |
| **Data** | Data access & transformation | `MovieRepositoryImpl` |
| **Data Source** | External APIs, databases | `MovieApiService` |

### 3. **Abstraction via Interfaces**

Domain layer defines **what** needs to be done, not **how**:

```kotlin
// domain/repository/MovieRepository.kt
interface MovieRepository {
    fun getPopularMovies(): Flow<Result<List<Movie>>>
}

// data/repository/MovieRepositoryImpl.kt
class MovieRepositoryImpl(
    private val apiService: MovieApiService
) : MovieRepository {
    override fun getPopularMovies() = flow {
        // Implementation details hidden
    }
}
```

### 4. **Use Cases (Single Responsibility)**

Each use case has **one reason to change**:

```kotlin
class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<Result<List<Movie>>> {
        return repository.getPopularMovies()
    }
}
```

## 🔧 Technology Stack

- **Language**: Kotlin 2.0.21
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt (Dagger)
- **Networking**: Retrofit + OkHttp
- **Async**: Coroutines + Flow
- **Image Loading**: Coil
- **Navigation**: Navigation Compose

## 🚀 Getting Started

### Prerequisites

1. Android Studio Ladybug or newer
2. JDK 11 or higher
3. Get a free API key from [The Movie Database (TMDB)](https://www.themoviedb.org/settings/api)

### Setup

1. **Clone the repository**

```bash
git clone <your-repo-url>
cd MovieApp
```

2. **Add your TMDB API Key**

Open `app/build.gradle.kts` and replace:

```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"YOUR_API_KEY_HERE\"")
```

Or add to `local.properties`:

```properties
TMDB_API_KEY=your_api_key_here
```

And update `app/build.gradle.kts`:

```kotlin
val apiKey = project.findProperty("TMDB_API_KEY") as String? ?: ""
buildConfigField("String", "TMDB_API_KEY", "\"$apiKey\"")
```

3. **Sync Gradle**

```bash
./gradlew build
```

4. **Run the app**

```bash
./gradlew installDebug
```

## 📁 Project Structure

```
MovieApp/
├── app/                          # Application module
│   └── src/main/java/com/aryandi/movieapp/
│       ├── di/                   # Dependency Injection
│       │   └── AppModule.kt
│       ├── presentation/         # ViewModels & UI
│       │   └── movies/
│       │       ├── MoviesViewModel.kt
│       │       └── MoviesScreen.kt
│       ├── MainActivity.kt
│       └── MovieApp.kt           # Application class
│
├── domain/                       # Pure Kotlin module
│   └── src/main/java/com/aryandi/movieapp/domain/
│       ├── model/                # Domain entities
│       │   └── Movie.kt
│       ├── repository/           # Repository interfaces
│       │   └── MovieRepository.kt
│       ├── usecase/              # Business logic
│       │   ├── GetPopularMoviesUseCase.kt
│       │   ├── GetMovieDetailsUseCase.kt
│       │   └── SearchMoviesUseCase.kt
│       └── common/               # Common domain classes
│           └── Result.kt
│
├── data/                         # Data module
│   └── src/main/java/com/aryandi/movieapp/data/
│       ├── remote/               # Remote data source
│       │   ├── api/
│       │   │   └── MovieApiService.kt
│       │   └── dto/              # Data Transfer Objects
│       │       ├── MovieDto.kt
│       │       └── MovieListResponse.kt
│       ├── repository/           # Repository implementation
│       │   └── MovieRepositoryImpl.kt
│       └── mapper/               # DTO ↔ Domain mappers
│           └── MovieMapper.kt
│
└── core/                         # Shared UI components
    └── src/main/java/com/aryandi/movieapp/core/
        └── ui/components/
            ├── MovieCard.kt
            ├── LoadingIndicator.kt
            └── ErrorMessage.kt
```

## 🧪 Testing Strategy

### Domain Layer (Unit Tests)

```kotlin
class GetPopularMoviesUseCaseTest {
    @Test
    fun `should return movies from repository`() {
        // Pure Kotlin testing - no Android dependencies
        val mockRepository = mock<MovieRepository>()
        val useCase = GetPopularMoviesUseCase(mockRepository)
        
        // Test business logic
    }
}
```

### Data Layer (Unit Tests)

```kotlin
class MovieRepositoryImplTest {
    @Test
    fun `should map API response to domain model`() {
        // Test data transformation
    }
}
```

### Presentation Layer (Unit Tests)

```kotlin
class MoviesViewModelTest {
    @Test
    fun `should emit success state when movies are loaded`() {
        // Test ViewModel logic
    }
}
```

### UI Layer (UI Tests)

```kotlin
@Test
fun moviesScreen_displaysMovieList() {
    // Compose UI testing
}
```

## 🎨 Key Features

### ✅ What's Included

- ✅ Clean Architecture with 4 modules
- ✅ MVVM pattern with Jetpack Compose
- ✅ Hilt Dependency Injection
- ✅ Retrofit for networking
- ✅ Coroutines + Flow for async operations
- ✅ Repository pattern
- ✅ Use case pattern
- ✅ Data mapping (DTO ↔ Domain)
- ✅ Error handling with `Result` wrapper
- ✅ Navigation with Jetpack Navigation Compose
- ✅ Reusable UI components
- ✅ Material 3 design

### 🚧 What You Can Add

- Room database for local caching
- Paging 3 for infinite scroll
- WorkManager for background tasks
- DataStore for preferences
- Unit & UI tests
- CI/CD pipeline
- More features (favorites, search, filters)

## 📚 Learning Resources

- [Clean Architecture Book by Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Clean Architecture for Android by Eran Boudjnah](https://github.com/EranBoudjnah/CleanArchitectureForAndroid)
- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- [Now in Android App](https://github.com/android/nowinandroid)

## 🤝 Contributing

This is a learning skeleton project. Feel free to:

1. Fork it
2. Modify it to your needs
3. Add features following Clean Architecture principles
4. Share improvements

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 🙏 Acknowledgments

This skeleton is inspired by:

- [Eran Boudjnah's Clean Architecture for Android](https://github.com/EranBoudjnah/CleanArchitectureForAndroid)
- Uncle Bob's Clean Architecture principles
- Android's official architecture recommendations

---

**Happy Coding! 🎉**

For questions or suggestions, feel free to open an issue or contribute!
