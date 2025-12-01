# 🏗️ Base Classes Guide

## Overview

This project uses base classes to promote **code reuse**, **consistency**, and **best practices**
across the Clean Architecture layers.

---

## 📦 Base Classes by Layer

### 1️⃣ **Domain Layer** - Business Logic

#### `BaseUseCase<Params, ReturnType>`

**Location**: `domain/src/main/java/com/aryandi/movieapp/domain/usecase/BaseUseCase.kt`

**Purpose**: Base class for all use cases with parameters

**Features**:

- ✅ Automatic error handling
- ✅ Threading management (coroutine dispatcher)
- ✅ Consistent Flow<Result<T>> return type
- ✅ Catch and wrap exceptions

**Usage Example**:

```kotlin
class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) : BaseUseCase<Int, Movie>() {
    
    override fun execute(params: Int): Flow<Result<Movie>> {
        return repository.getMovieDetails(params)
    }
}

// Usage in ViewModel
getMovieDetailsUseCase(movieId = 123).collect { result ->
    // Handle result
}
```

**Benefits**:

- No need to repeat error handling code
- Consistent API across all use cases
- Easy to add cross-cutting concerns (logging, analytics)

---

#### `BaseUseCaseNoParams<ReturnType>`

**Location**: Same file as `BaseUseCase`

**Purpose**: Base class for use cases without parameters

**Usage Example**:

```kotlin
class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) : BaseUseCaseNoParams<List<Movie>>() {
    
    override fun execute(): Flow<Result<List<Movie>>> {
        return repository.getPopularMovies()
    }
}

// Usage in ViewModel
getPopularMoviesUseCase().collect { result ->
    // Handle result
}
```

---

#### `BaseSuspendUseCase<Params, ReturnType>`

**Location**: Same file as `BaseUseCase`

**Purpose**: For one-time operations (not streaming)

**Usage Example**:

```kotlin
class SaveMovieUseCase(
    private val repository: MovieRepository
) : BaseSuspendUseCase<Movie, Unit>() {
    
    override suspend fun execute(params: Movie): Result<Unit> {
        return repository.saveMovie(params)
    }
}

// Usage in ViewModel
val result = saveMovieUseCase(movie)
```

---

### 2️⃣ **Data Layer** - Data Access

#### `BaseRepository`

**Location**: `data/src/main/java/com/aryandi/movieapp/data/base/BaseRepository.kt`

**Purpose**: Common data access patterns and error handling

**Features**:

- ✅ Safe API call wrapper
- ✅ Network-first strategy
- ✅ Cache-first strategy
- ✅ Automatic loading state emission

**Usage Example**:

```kotlin
class MovieRepositoryImpl(
    private val apiService: MovieApiService
) : BaseRepository(), MovieRepository {
    
    // Simple API call with automatic error handling
    override fun getPopularMovies(): Flow<Result<List<Movie>>> {
        return safeApiCall {
            val response = apiService.getPopularMovies()
            response.results.toDomain()
        }
    }
    
    // Network-first with cache fallback
    override fun getMovieWithCache(id: Int): Flow<Result<Movie>> {
        return networkFirst(
            fetchFromNetwork = { 
                apiService.getMovie(id).toDomain() 
            },
            fetchFromCache = { 
                database.getMovie(id)?.toDomain() 
            },
            saveToCache = { movie -> 
                database.saveMovie(movie.toEntity()) 
            }
        )
    }
}
```

**Benefits**:

- No repeated try-catch blocks
- Consistent error handling
- Easy to implement offline-first

---

#### `DataToDomainMapper<DataModel, DomainModel>`

**Location**: Same file as `BaseRepository`

**Purpose**: Type-safe mapping between layers

**Usage Example**:

```kotlin
object MovieMapper : DataToDomainMapper<MovieDto, Movie> {
    override fun toDomain(dataModel: MovieDto): Movie {
        return Movie(
            id = dataModel.id,
            title = dataModel.title,
            // ... map all fields
        )
    }
}

// Use with extension function
val movies = dtoList.toDomain(MovieMapper)
```

---

### 3️⃣ **Presentation Layer** - UI Logic

#### `BaseViewModel<State, Event>`

**Location**: `app/src/main/java/com/aryandi/movieapp/presentation/base/BaseViewModel.kt`

**Purpose**: Common ViewModel functionality with state and event management

**Features**:

- ✅ State management (StateFlow)
- ✅ Event handling (SharedFlow for one-time events)
- ✅ Error handling
- ✅ Coroutine scope management

**Usage Example**:

```kotlin
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : BaseViewModel<MoviesUiState, MoviesUiEvent>() {
    
    override fun initialState() = MoviesUiState.Loading
    
    fun loadMovies() {
        launchWithHandler {
            getPopularMoviesUseCase().collect { result ->
                setState {
                    when (result) {
                        is Result.Loading -> MoviesUiState.Loading
                        is Result.Success -> MoviesUiState.Success(result.data)
                        is Result.Error -> MoviesUiState.Error(result.exception.message)
                    }
                }
            }
        }
    }
    
    fun onMovieClick(movieId: Int) {
        sendEvent(MoviesUiEvent.NavigateToDetails(movieId))
    }
}

// Define states
sealed class MoviesUiState : UiState {
    object Loading : MoviesUiState()
    data class Success(val movies: List<Movie>) : MoviesUiState()
    data class Error(val message: String) : MoviesUiState()
}

// Define events
sealed class MoviesUiEvent : UiEvent {
    data class NavigateToDetails(val movieId: Int) : MoviesUiEvent()
}
```

**Benefits**:

- Consistent state management
- Separation of state and events
- Automatic error handling
- No memory leaks (viewModelScope)

---

#### Marker Interfaces

**`UiState`**: All UI states must implement this

```kotlin
sealed class MyScreenState : UiState {
    object Loading : MyScreenState()
    data class Success(val data: Data) : MyScreenState()
}
```

**`UiEvent`**: All UI events must implement this

```kotlin
sealed class MyScreenEvent : UiEvent {
    object NavigateBack : MyScreenEvent()
    data class ShowToast(val message: String) : MyScreenEvent()
}
```

---

### 4️⃣ **UI Layer** - Compose Components

#### `BaseScreen<T>`

**Location**: `core/src/main/java/com/aryandi/movieapp/core/base/BaseScreen.kt`

**Purpose**: Common UI patterns for screens

**Features**:

- ✅ Loading state handling
- ✅ Error state handling
- ✅ Empty state handling
- ✅ Success content

**Usage Example**:

```kotlin
@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    
    // Convert to ScreenState
    val screenState = when (state) {
        is MoviesUiState.Loading -> ScreenState.Loading
        is MoviesUiState.Success -> ScreenState.Success(state.movies)
        is MoviesUiState.Error -> ScreenState.Error(state.message)
        is MoviesUiState.Empty -> ScreenState.Empty()
    }
    
    BaseScreen(
        state = screenState,
        onRetry = { viewModel.loadMovies() }
    ) { movies ->
        // Your success UI
        MovieList(movies = movies)
    }
}
```

---

#### `ObserveEvents`

**Purpose**: Collect one-time UI events

**Usage Example**:

```kotlin
@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    navController: NavController
) {
    // Observe events
    ObserveEvents(events = viewModel.uiEvent) { event ->
        when (event) {
            is MoviesUiEvent.NavigateToDetails -> {
                navController.navigate("details/${event.movieId}")
            }
            is MoviesUiEvent.ShowToast -> {
                // Show toast
            }
        }
    }
    
    // Rest of your screen
}
```

---

## 🎯 When to Use Each Base Class

### Use `BaseUseCase` when:

- ✅ Your use case needs parameters
- ✅ You want streaming results (Flow)
- ✅ You need automatic error handling
- ✅ Example: `GetMovieDetailsUseCase(movieId)`

### Use `BaseUseCaseNoParams` when:

- ✅ Your use case has no parameters
- ✅ You want streaming results (Flow)
- ✅ Example: `GetPopularMoviesUseCase()`

### Use `BaseSuspendUseCase` when:

- ✅ One-time operation (not streaming)
- ✅ Example: `SaveMovieUseCase(movie)`

### Use `BaseRepository` when:

- ✅ Implementing any repository
- ✅ Need safe API calls
- ✅ Want offline-first capabilities
- ✅ Example: All repository implementations

### Use `BaseViewModel` when:

- ✅ Creating any ViewModel
- ✅ Need state management
- ✅ Need event handling
- ✅ Example: All ViewModels

### Use `BaseScreen` when:

- ✅ Screen has loading/error/empty states
- ✅ Want consistent UI patterns
- ✅ Example: List screens, detail screens

---

## 🎨 Architecture Diagram with Base Classes

```
┌─────────────────────────────────────┐
│          UI Layer                   │
│  BaseScreen, ObserveEvents          │
│  ↓ uses                             │
│  Composable Functions               │
└─────────────────────────────────────┘
                ↓ observes
┌─────────────────────────────────────┐
│      Presentation Layer             │
│  BaseViewModel<State, Event>        │
│  ↓ extends                          │
│  MoviesViewModel                    │
└─────────────────────────────────────┘
                ↓ executes
┌─────────────────────────────────────┐
│        Domain Layer                 │
│  BaseUseCase<Params, Return>        │
│  ↓ extends                          │
│  GetMoviesUseCase                   │
│  ↓ calls                            │
│  Repository Interface               │
└─────────────────────────────────────┘
                ↓ implemented by
┌─────────────────────────────────────┐
│         Data Layer                  │
│  BaseRepository                     │
│  ↓ extends                          │
│  MovieRepositoryImpl                │
│  ↓ uses                             │
│  DataToDomainMapper                 │
└─────────────────────────────────────┘
```

---

## ✅ Benefits of Base Classes

### 1. **Code Reuse**

- Write common logic once
- Use across all features
- Reduce boilerplate

### 2. **Consistency**

- Same patterns everywhere
- Easy to understand
- Predictable behavior

### 3. **Maintainability**

- Fix bugs in one place
- Easy to update
- Clear structure

### 4. **Testability**

- Mock base functionality
- Test concrete implementation
- Isolated unit tests

### 5. **Scalability**

- Easy to add features
- Follow established patterns
- Onboard new developers faster

---

## 📝 Best Practices

### DO ✅

- ✅ Extend base classes for new features
- ✅ Follow the established patterns
- ✅ Add specific logic in concrete classes
- ✅ Document deviations from base behavior

### DON'T ❌

- ❌ Copy-paste instead of extending
- ❌ Add too much logic in base classes
- ❌ Create base classes for everything
- ❌ Violate layer boundaries

---

## 🔍 Examples in Project

### Current Implementations:

**Use Cases**:

- ✅ `GetPopularMoviesUseCase` extends `BaseUseCaseNoParams`
- ✅ `GetMovieDetailsUseCase` extends `BaseUseCase<Int, Movie>`
- ✅ `SearchMoviesUseCase` extends `BaseUseCase<String, List<Movie>>`

**Repositories**:

- ✅ `MovieRepositoryImpl` extends `BaseRepository`

**ViewModels**:

- ✅ `MoviesViewModel` extends `BaseViewModel<MoviesUiState, MoviesUiEvent>`

**Mappers**:

- ✅ `MovieMapper` implements `DataToDomainMapper<MovieDto, Movie>`

---

## 🚀 Quick Start: Adding a New Feature

### 1. Create Domain Use Case

```kotlin
class GetTrendingMoviesUseCase(
    private val repository: MovieRepository
) : BaseUseCaseNoParams<List<Movie>>() {
    override fun execute() = repository.getTrendingMovies()
}
```

### 2. Update Repository

```kotlin
class MovieRepositoryImpl : BaseRepository(), MovieRepository {
    override fun getTrendingMovies() = safeApiCall {
        apiService.getTrending().results.toDomain()
    }
}
```

### 3. Create ViewModel

```kotlin
@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val getTrendingUseCase: GetTrendingMoviesUseCase
) : BaseViewModel<TrendingUiState, TrendingUiEvent>() {
    override fun initialState() = TrendingUiState.Loading
    // ... implement
}
```

### 4. Create Screen

```kotlin
@Composable
fun TrendingScreen(viewModel: TrendingViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    BaseScreen(
        state = state.toScreenState(),
        onRetry = { viewModel.loadTrending() }
    ) { movies ->
        // Your UI
    }
}
```

---

## 📚 Related Documentation

- [ARCHITECTURE.md](ARCHITECTURE.md) - Overall architecture
- [CHEATSHEET.md](CHEATSHEET.md) - Quick reference
- [FILE_STRUCTURE.md](FILE_STRUCTURE.md) - Project structure

---

## 🎓 Summary

**Base Classes Provided**:

1. ✅ `BaseUseCase` - Domain layer use cases
2. ✅ `BaseRepository` - Data layer repositories
3. ✅ `BaseViewModel` - Presentation layer ViewModels
4. ✅ `BaseScreen` - UI layer Composables
5. ✅ `DataToDomainMapper` - Data mapping

**Key Principles**:

- 🎯 Single Responsibility
- 🔄 Code Reuse
- 📏 Consistency
- 🧪 Testability
- 📈 Scalability

**Remember**: Base classes are tools, not rules. Use them when they add value!

---

**Happy Coding with Base Classes!** 🏗️
