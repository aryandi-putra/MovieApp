# Complete File Structure

## 📁 Full Project Tree

```
MovieApp/
│
├── 📄 .gitignore                           # Git ignore rules
├── 📖 README.md                            # Main documentation
├── 📖 ARCHITECTURE.md                      # Architecture deep dive
├── 📖 SETUP_GUIDE.md                       # Setup instructions
├── 📖 SUMMARY.md                           # Project summary
├── 📖 DIAGRAMS.md                          # Visual diagrams
├── 📖 CHEATSHEET.md                        # Quick reference
├── 📖 FILE_STRUCTURE.md                    # This file
│
├── 📦 build.gradle.kts                     # Root build file
├── 📦 settings.gradle.kts                  # Module includes
├── 📦 gradle.properties                    # Gradle settings
│
├── 📁 gradle/
│   ├── libs.versions.toml                  # Version catalog
│   └── wrapper/                            # Gradle wrapper
│
├── 📱 app/                                 # Application module
│   ├── 📦 build.gradle.kts                # App build config
│   ├── 📄 proguard-rules.pro              # Proguard rules
│   │
│   └── 📁 src/
│       ├── 📁 main/
│       │   ├── 📄 AndroidManifest.xml     # App manifest
│       │   │
│       │   ├── 📁 java/com/aryandi/movieapp/
│       │   │   │
│       │   │   ├── 📁 di/                 # Dependency Injection
│       │   │   │   └── 🔧 AppModule.kt   # Hilt modules
│       │   │   │
│       │   │   ├── 📁 presentation/       # MVVM Layer
│       │   │   │   └── 📁 movies/
│       │   │   │       ├── 🎨 MoviesScreen.kt     # Compose UI
│       │   │   │       └── 🧠 MoviesViewModel.kt  # State mgmt
│       │   │   │
│       │   │   ├── 📁 ui/theme/           # Theme files
│       │   │   │   ├── Color.kt
│       │   │   │   ├── Theme.kt
│       │   │   │   └── Type.kt
│       │   │   │
│       │   │   ├── 🚀 MainActivity.kt     # Entry point
│       │   │   └── 📱 MovieApp.kt         # Application class
│       │   │
│       │   └── 📁 res/                    # Resources
│       │       ├── drawable/
│       │       ├── mipmap-*/              # App icons
│       │       ├── values/
│       │       │   ├── colors.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       └── xml/
│       │
│       ├── 📁 test/                       # Unit tests
│       │   └── java/com/aryandi/movieapp/
│       │       └── ExampleUnitTest.kt
│       │
│       └── 📁 androidTest/                # UI tests
│           └── java/com/aryandi/movieapp/
│               └── ExampleInstrumentedTest.kt
│
├── 💎 domain/                             # Domain module (Pure Kotlin)
│   ├── 📦 build.gradle.kts               # Domain build config
│   │
│   └── 📁 src/
│       ├── 📁 main/java/com/aryandi/movieapp/domain/
│       │   │
│       │   ├── 📁 model/                 # Domain entities
│       │   │   └── 🎬 Movie.kt          # Business entity
│       │   │
│       │   ├── 📁 repository/            # Repository contracts
│       │   │   └── 📋 MovieRepository.kt # Interface
│       │   │
│       │   ├── 📁 usecase/               # Business logic
│       │   │   ├── 🎯 GetPopularMoviesUseCase.kt
│       │   │   ├── 🎯 GetMovieDetailsUseCase.kt
│       │   │   └── 🎯 SearchMoviesUseCase.kt
│       │   │
│       │   └── 📁 common/                # Common classes
│       │       └── 📦 Result.kt         # Result wrapper
│       │
│       └── 📁 test/                      # Pure Kotlin tests
│           └── java/com/aryandi/movieapp/domain/
│
├── 💾 data/                               # Data module
│   ├── 📦 build.gradle.kts               # Data build config
│   │
│   └── 📁 src/
│       ├── 📁 main/java/com/aryandi/movieapp/data/
│       │   │
│       │   ├── 📁 remote/                # Remote data source
│       │   │   │
│       │   │   ├── 📁 api/
│       │   │   │   └── 🌐 MovieApiService.kt  # Retrofit API
│       │   │   │
│       │   │   └── 📁 dto/               # Data Transfer Objects
│       │   │       ├── 📄 MovieDto.kt
│       │   │       └── 📄 MovieListResponse.kt
│       │   │
│       │   ├── 📁 mapper/                # Data transformations
│       │   │   └── 🔄 MovieMapper.kt    # DTO ↔ Domain
│       │   │
│       │   └── 📁 repository/            # Repository impl
│       │       └── 🏪 MovieRepositoryImpl.kt
│       │
│       └── 📁 test/                      # Data layer tests
│           └── java/com/aryandi/movieapp/data/
│
└── 🎨 core/                               # Core module (Shared)
    ├── 📦 build.gradle.kts               # Core build config
    │
    └── 📁 src/
        └── 📁 main/java/com/aryandi/movieapp/core/
            └── 📁 ui/
                └── 📁 components/        # Reusable components
                    ├── 🎴 MovieCard.kt
                    ├── ⏳ LoadingIndicator.kt
                    └── ⚠️ ErrorMessage.kt
```

## 📊 File Count by Module

| Module | Kotlin Files | Purpose |
|--------|--------------|---------|
| **app** | 5 | UI + Presentation + DI |
| **domain** | 6 | Business logic (Pure Kotlin) |
| **data** | 5 | Data access & transformation |
| **core** | 3 | Shared UI components |
| **docs** | 6 | Documentation files |
| **Total** | 25 | Complete skeleton |

## 🎯 Key Files Explained

### 🏗️ Build & Configuration

#### `build.gradle.kts` (Root)

```kotlin
// Root build configuration
plugins {
    // Plugin versions
}
```

#### `settings.gradle.kts`

```kotlin
// Module includes
include(":app")
include(":domain")
include(":data")
include(":core")
```

#### `gradle/libs.versions.toml`

```toml
[versions]
# Version catalog for all dependencies
kotlin = "2.0.21"
compose = "2024.09.00"
hilt = "2.52"
# ...

[libraries]
# Dependency declarations

[plugins]
# Plugin declarations
```

### 📱 App Module

#### `MovieApp.kt`

```kotlin
@HiltAndroidApp
class MovieApp : Application()
```

- Application class
- Enables Hilt dependency injection
- Entry point for the app

#### `MainActivity.kt`

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Setup Navigation
    // Configure Compose
}
```

- Single activity
- Navigation setup
- Compose configuration

#### `di/AppModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Provides all dependencies
}
```

- Hilt dependency injection
- Provides Retrofit, Repository, Use Cases
- Singleton scope

#### `presentation/movies/MoviesViewModel.kt`

```kotlin
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel()
```

- MVVM ViewModel
- Manages UI state
- Executes use cases
- Survives configuration changes

#### `presentation/movies/MoviesScreen.kt`

```kotlin
@Composable
fun MoviesScreen(viewModel: MoviesViewModel = hiltViewModel())
```

- Jetpack Compose UI
- Observes ViewModel state
- Displays movies in grid
- Handles user interactions

### 💎 Domain Module (Pure Kotlin)

#### `model/Movie.kt`

```kotlin
data class Movie(
    val id: Int,
    val title: String,
    // Pure domain model
)
```

- Core business entity
- No framework dependencies
- Represents movie concept

#### `repository/MovieRepository.kt`

```kotlin
interface MovieRepository {
    fun getPopularMovies(): Flow<Result<List<Movie>>>
}
```

- Repository contract
- Defines data operations
- Abstraction for data layer

#### `usecase/GetPopularMoviesUseCase.kt`

```kotlin
class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<Result<List<Movie>>>
}
```

- Single business operation
- Encapsulates business logic
- Coordinates data flow

#### `common/Result.kt`

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

- Type-safe result wrapper
- Handles success/error/loading states
- Used throughout the app

### 💾 Data Module

#### `remote/api/MovieApiService.kt`

```kotlin
interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieListResponse
}
```

- Retrofit API interface
- Defines TMDB endpoints
- Network calls

#### `remote/dto/MovieDto.kt`

```kotlin
data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    // API response structure
)
```

- Data Transfer Object
- Matches API structure
- Framework-specific annotations

#### `mapper/MovieMapper.kt`

```kotlin
object MovieMapper {
    fun MovieDto.toDomain(): Movie
    fun List<MovieDto>.toDomain(): List<Movie>
}
```

- Converts DTOs to domain models
- Keeps layers independent
- Transformation logic

#### `repository/MovieRepositoryImpl.kt`

```kotlin
class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : MovieRepository {
    // Implements repository interface
}
```

- Concrete implementation
- Fetches data from API
- Maps DTOs to domain models
- Error handling

### 🎨 Core Module

#### `ui/components/MovieCard.kt`

```kotlin
@Composable
fun MovieCard(
    title: String,
    posterPath: String?,
    rating: Double,
    onClick: () -> Unit
)
```

- Reusable movie card
- Displays poster, title, rating
- Handles click events
- Uses Coil for image loading

#### `ui/components/LoadingIndicator.kt`

```kotlin
@Composable
fun LoadingIndicator()
```

- Shows loading state
- Centered progress indicator
- Reusable across features

#### `ui/components/ErrorMessage.kt`

```kotlin
@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
)
```

- Displays error state
- Shows error message
- Provides retry button
- Consistent error UI

## 📚 Documentation Files

| File | Purpose | Audience |
|------|---------|----------|
| **README.md** | Overview & quick start | Everyone |
| **ARCHITECTURE.md** | Detailed architecture explanation | Developers learning |
| **SETUP_GUIDE.md** | Step-by-step setup | Getting started |
| **SUMMARY.md** | Project summary | Quick overview |
| **DIAGRAMS.md** | Visual architecture diagrams | Visual learners |
| **CHEATSHEET.md** | Quick reference templates | Daily development |
| **FILE_STRUCTURE.md** | This file - complete structure | Understanding layout |

## 🔄 Data Flow Through Files

### Example: Loading Popular Movies

```
1. User opens app
   ↓
2. MainActivity.kt creates MoviesScreen

3. MoviesScreen.kt observes MoviesViewModel

4. MoviesViewModel.kt (init block)
   ↓
5. Calls GetPopularMoviesUseCase.invoke()

6. GetPopularMoviesUseCase.kt
   ↓
7. Calls MovieRepository.getPopularMovies()

8. MovieRepositoryImpl.kt
   ↓
9. Calls MovieApiService.getPopularMovies()

10. API returns MovieListResponse

11. MovieMapper.kt converts MovieDto → Movie

12. Result flows back up:
    MovieRepositoryImpl → UseCase → ViewModel

13. ViewModel updates _uiState

14. MoviesScreen observes state change

15. UI recomposes with movies
```

## 🎯 Where to Find What

Need to...

### Add a new API endpoint?

→ `data/remote/api/MovieApiService.kt`

### Create a new entity?

→ `domain/model/NewEntity.kt`

### Add business logic?

→ `domain/usecase/NewUseCase.kt`

### Create a new screen?

→ `app/presentation/newfeature/NewScreen.kt`

### Add a reusable component?

→ `core/ui/components/NewComponent.kt`

### Configure dependencies?

→ `app/di/AppModule.kt`

### Update app theme?

→ `app/ui/theme/Theme.kt`

### Add navigation?

→ `app/MainActivity.kt`

## 🧪 Test File Locations

```
Unit Tests (No Android):
├── domain/src/test/java/.../
│   └── UseCaseTest.kt
│
└── data/src/test/java/.../
    └── RepositoryTest.kt

Unit Tests (Android):
└── app/src/test/java/.../
    └── ViewModelTest.kt

UI Tests (Instrumentation):
└── app/src/androidTest/java/.../
    └── ScreenTest.kt
```

## 📦 Module Comparison

| Aspect | app | domain | data | core |
|--------|-----|--------|------|------|
| **Type** | Application | Library | Library | Library |
| **Language** | Kotlin | Pure Kotlin | Kotlin | Kotlin |
| **Android Deps** | Yes | No | Yes | Yes |
| **Dependencies** | All | None | domain | None |
| **Testability** | Android Tests | Pure JUnit | Android Tests | Compose Tests |
| **Changes Most** | Often | Rarely | Sometimes | Sometimes |

## 🎨 Naming Pattern Summary

```
Entities:         Movie, User, Product
DTOs:             MovieDto, UserDto, ProductDto
Use Cases:        GetMoviesUseCase, SaveUserUseCase
Repositories:     MovieRepository (interface)
                  MovieRepositoryImpl (implementation)
ViewModels:       MoviesViewModel, ProfileViewModel
Screens:          MoviesScreen, ProfileScreen
UI States:        MoviesUiState, ProfileUiState
Components:       MovieCard, UserAvatar
Mappers:          MovieMapper, UserMapper
```

## 🚀 Build Outputs

```
app/build/outputs/
├── apk/
│   ├── debug/
│   │   └── app-debug.apk
│   └── release/
│       └── app-release.apk
│
└── logs/
    └── manifest-merger-*-report.txt
```

---

This structure represents a **minimal but complete** Clean Architecture skeleton that you can expand
upon!
