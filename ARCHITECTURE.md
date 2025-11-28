# Clean Architecture - Detailed Explanation

## 🎯 What is Clean Architecture?

Clean Architecture is a software design philosophy introduced by Robert C. Martin (Uncle Bob). It
emphasizes:

1. **Independence from frameworks** - Your business logic doesn't depend on external libraries
2. **Testability** - Business rules can be tested without UI, database, or external elements
3. **Independence from UI** - The UI can change without affecting business rules
4. **Independence from database** - You can swap databases without touching business logic
5. **Independence from external agencies** - Business rules don't know anything about interfaces to
   the outside world

## 🏛️ The Layers

### Layer 1: Domain (Innermost Circle)

**Location**: `domain/` module

**Purpose**: Contains the core business logic and business rules of your application.

**Characteristics**:

- **Pure Kotlin** (no Android dependencies)
- **Most stable** layer (changes least frequently)
- **No dependencies** on other modules
- **Framework independent**

**Components**:

#### Entities (Models)

```kotlin
// domain/model/Movie.kt
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    // ... domain-specific fields
)
```

- Represent core business concepts
- Pure data classes with business rules
- No framework annotations (no @SerializedName, @Entity, etc.)

#### Repository Interfaces

```kotlin
// domain/repository/MovieRepository.kt
interface MovieRepository {
    fun getPopularMovies(): Flow<Result<List<Movie>>>
}
```

- Define **what** operations are needed
- Don't specify **how** they're implemented
- Allow dependency inversion

#### Use Cases (Interactors)

```kotlin
// domain/usecase/GetPopularMoviesUseCase.kt
class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<Result<List<Movie>>> {
        return repository.getPopularMovies()
    }
}
```

- Encapsulate a single business operation
- Orchestrate data flow
- Contain business rules
- Single Responsibility Principle

**Why Pure Kotlin?**

- ✅ Can test with JUnit (no Android instrumentation needed)
- ✅ Can reuse on other platforms (Kotlin Multiplatform)
- ✅ Fastest test execution
- ✅ No Android framework coupling

---

### Layer 2: Data

**Location**: `data/` module

**Purpose**: Implements data operations defined by domain layer.

**Dependencies**: `domain` module

**Components**:

#### Data Transfer Objects (DTOs)

```kotlin
// data/remote/dto/MovieDto.kt
data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String
)
```

- Match external API/Database structure
- Framework-specific annotations allowed
- Separate from domain models

#### API Service

```kotlin
// data/remote/api/MovieApiService.kt
interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieListResponse
}
```

- Retrofit interfaces
- Define network endpoints

#### Mappers

```kotlin
// data/mapper/MovieMapper.kt
fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        // Map DTO fields to Domain fields
    )
}
```

- Convert DTOs ↔ Domain models
- Keep layers independent
- Transformation logic

#### Repository Implementation

```kotlin
// data/repository/MovieRepositoryImpl.kt
class MovieRepositoryImpl(
    private val apiService: MovieApiService,
    private val apiKey: String
) : MovieRepository {
    override fun getPopularMovies() = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getPopularMovies(apiKey)
            val movies = response.results.toDomain()
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
```

- Implements domain's repository interface
- Handles data sources (API, DB, Cache)
- Error handling
- Data transformation

**Data Layer Responsibilities**:

- ✅ Fetch data from multiple sources
- ✅ Cache management
- ✅ Data synchronization
- ✅ Mapping between layers

---

### Layer 3: Presentation

**Location**: `app/presentation/` package

**Purpose**: Manages UI state and handles user interactions.

**Dependencies**: `domain` module (not `data`!)

**Components**:

#### UI State

```kotlin
// app/presentation/movies/MoviesUiState.kt
sealed class MoviesUiState {
    object Loading : MoviesUiState()
    data class Success(val movies: List<Movie>) : MoviesUiState()
    data class Error(val message: String) : MoviesUiState()
}
```

- Represents all possible UI states
- Immutable data classes
- Type-safe state management

#### ViewModel

```kotlin
// app/presentation/movies/MoviesViewModel.kt
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<MoviesUiState>(Loading)
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()
    
    init {
        loadMovies()
    }
    
    fun loadMovies() {
        viewModelScope.launch {
            getPopularMoviesUseCase().collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> MoviesUiState.Loading
                    is Result.Success -> MoviesUiState.Success(result.data)
                    is Result.Error -> MoviesUiState.Error(result.exception.message)
                }
            }
        }
    }
}
```

**ViewModel Responsibilities**:

- ✅ Execute use cases
- ✅ Manage UI state
- ✅ Handle user actions
- ✅ Survive configuration changes
- ❌ **Does NOT** contain business logic
- ❌ **Does NOT** access repositories directly

---

### Layer 4: UI

**Location**: `app/presentation/*/` screens

**Purpose**: Display data and handle user input.

**Dependencies**: Presentation layer (ViewModels)

```kotlin
// app/presentation/movies/MoviesScreen.kt
@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (val state = uiState) {
        is Loading -> LoadingIndicator()
        is Success -> MovieList(state.movies)
        is Error -> ErrorMessage(state.message)
    }
}
```

**UI Layer Responsibilities**:

- ✅ Observe ViewModel state
- ✅ Display data
- ✅ Handle user interactions
- ✅ Navigation
- ❌ **Does NOT** contain business logic
- ❌ **Does NOT** call use cases directly

---

### Layer 5: Core (Shared)

**Location**: `core/` module

**Purpose**: Shared components used across features.

**No dependencies** on other modules

**Contents**:

- Reusable Composables
- Extension functions
- Common utilities
- Design system components

---

## 🔄 Data Flow

### Reading Data (Bottom-Up)

```
UI
 ↓ observes
ViewModel
 ↓ executes
Use Case
 ↓ calls
Repository Interface (Domain)
 ↑ implemented by
Repository Implementation (Data)
 ↓ calls
API Service / Database
 ↓ returns
DTO
 ↓ mapped to
Domain Model
 ↑ returned through layers
UI (displays)
```

### User Action (Top-Down)

```
User taps button
 ↓
UI calls ViewModel method
 ↓
ViewModel executes Use Case
 ↓
Use Case calls Repository
 ↓
Repository saves to API/DB
 ↓
Result flows back up
 ↓
UI updates
```

---

## 🎯 Key Principles

### 1. Dependency Inversion Principle (DIP)

**High-level modules should not depend on low-level modules. Both should depend on abstractions.**

❌ **Bad**:

```kotlin
// ViewModel depends on concrete repository
class MoviesViewModel(
    private val repository: MovieRepositoryImpl // Concrete!
)
```

✅ **Good**:

```kotlin
// ViewModel depends on abstraction
class MoviesViewModel(
    private val repository: MovieRepository // Interface!
)
```

### 2. Single Responsibility Principle (SRP)

**Each class should have only one reason to change.**

✅ **Use Cases are small and focused**:

```kotlin
class GetPopularMoviesUseCase // Only gets popular movies
class SearchMoviesUseCase      // Only searches movies
class GetMovieDetailsUseCase   // Only gets movie details
```

### 3. Open/Closed Principle

**Open for extension, closed for modification.**

You can add new features without modifying existing code:

```kotlin
// Add new use case without touching existing ones
class GetTrendingMoviesUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke() = repository.getTrendingMovies()
}
```

### 4. Interface Segregation Principle

**Clients should not depend on interfaces they don't use.**

Split large interfaces into smaller, focused ones:

```kotlin
interface MovieReader {
    fun getMovies(): Flow<Result<List<Movie>>>
}

interface MovieWriter {
    fun saveMovie(movie: Movie): Flow<Result<Unit>>
}
```

---

## ✅ Benefits of Clean Architecture

### 1. Testability

```kotlin
// Test use case with mocked repository
@Test
fun `should return movies from repository`() {
    val mockRepo = mock<MovieRepository>()
    val useCase = GetPopularMoviesUseCase(mockRepo)
    
    // Test pure logic - no Android dependencies!
}
```

### 2. Maintainability

- Clear boundaries between layers
- Easy to locate and fix bugs
- Changes in one layer don't affect others

### 3. Scalability

- Easy to add new features
- Multiple developers can work on different layers simultaneously
- Clear module boundaries

### 4. Flexibility

- Swap implementations easily
- Change UI framework (XML → Compose)
- Change database (Room → SQLDelight)
- Change API library (Retrofit → Ktor)

### 5. Reusability

- Domain logic can be shared across platforms
- Use cases can be reused in different features
- Shared core components

---

## 🚨 Common Mistakes to Avoid

### ❌ Don't bypass layers

```kotlin
// BAD: ViewModel calling API directly
class ViewModel(private val api: MovieApiService)

// GOOD: ViewModel → Use Case → Repository → API
class ViewModel(private val useCase: GetPopularMoviesUseCase)
```

### ❌ Don't put Android dependencies in domain

```kotlin
// BAD: Android framework in domain
class GetMoviesUseCase(private val context: Context)

// GOOD: Pure Kotlin
class GetMoviesUseCase(private val repository: MovieRepository)
```

### ❌ Don't reuse DTOs as domain models

```kotlin
// BAD: Using DTO in UI
data class MovieDto(@SerializedName("id") val id: Int)
fun displayMovie(movie: MovieDto) // Coupled to API!

// GOOD: Separate models
data class Movie(val id: Int) // Domain
fun displayMovie(movie: Movie) // Independent!
```

### ❌ Don't put business logic in ViewModels

```kotlin
// BAD: Business logic in ViewModel
class ViewModel {
    fun loadMovies() {
        if (movies.size > 10) { // Business rule!
            filterMovies()
        }
    }
}

// GOOD: Business logic in Use Case
class GetMoviesUseCase {
    fun invoke(): Flow<List<Movie>> {
        return repository.getMovies()
            .map { movies ->
                if (movies.size > 10) movies.take(10)
                else movies
            }
    }
}
```

---

## 📚 Further Reading

1. [Clean Architecture Blog by Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
2. [Clean Architecture for Android by Eran Boudjnah](https://github.com/EranBoudjnah/CleanArchitectureForAndroid)
3. [Android Architecture Guide](https://developer.android.com/topic/architecture)
4. [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)

---

**Remember**: Clean Architecture is about making your code:

- 📖 **Readable** - Easy to understand
- 🧪 **Testable** - Easy to test
- 🔄 **Maintainable** - Easy to change
- 📈 **Scalable** - Easy to grow
