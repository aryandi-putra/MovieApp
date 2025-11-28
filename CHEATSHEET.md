# Clean Architecture Cheat Sheet

## 🎯 Quick Reference

### Layer Dependencies (Remember: Point INWARD ⬅️)

```
UI → Presentation → Domain ← Data ← DataSource
```

### Module Dependencies

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
}

// domain/build.gradle.kts
dependencies {
    // NO dependencies on other modules!
}
```

## 📦 When to Use Each Layer

| Layer | Use For | Don't Use For |
|-------|---------|---------------|
| **Domain** | Business logic, entities, repository interfaces | Android code, UI logic, framework code |
| **Data** | API calls, database, data transformation | Business logic, UI logic |
| **Presentation** | State management, UI logic | Business logic, direct API calls |
| **UI** | Display data, user input | Business logic, data fetching |
| **Core** | Shared components | Feature-specific logic |

## 🔧 Quick Templates

### 1. Create Domain Entity

```kotlin
// domain/model/YourEntity.kt
package com.aryandi.movieapp.domain.model

data class YourEntity(
    val id: Int,
    val name: String,
    // Only business-relevant fields
)
```

### 2. Create Use Case

```kotlin
// domain/usecase/YourUseCase.kt
package com.aryandi.movieapp.domain.usecase

class YourUseCase(
    private val repository: YourRepository
) {
    operator fun invoke(param: Type): Flow<Result<ReturnType>> {
        // Business logic here
        return repository.getData(param)
    }
}
```

### 3. Create Repository Interface

```kotlin
// domain/repository/YourRepository.kt
package com.aryandi.movieapp.domain.repository

interface YourRepository {
    fun getData(param: Type): Flow<Result<Data>>
}
```

### 4. Create DTO

```kotlin
// data/remote/dto/YourDto.kt
package com.aryandi.movieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class YourDto(
    @SerializedName("api_field_name")
    val fieldName: Type
)
```

### 5. Create Mapper

```kotlin
// data/mapper/YourMapper.kt
package com.aryandi.movieapp.data.mapper

import com.aryandi.movieapp.data.remote.dto.YourDto
import com.aryandi.movieapp.domain.model.YourEntity

object YourMapper {
    fun YourDto.toDomain(): YourEntity {
        return YourEntity(
            id = id,
            name = name
        )
    }
}
```

### 6. Implement Repository

```kotlin
// data/repository/YourRepositoryImpl.kt
package com.aryandi.movieapp.data.repository

class YourRepositoryImpl @Inject constructor(
    private val apiService: YourApiService
) : YourRepository {
    
    override fun getData(param: Type) = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getData(param)
            val data = response.toDomain()
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
```

### 7. Create ViewModel

```kotlin
// app/presentation/feature/YourViewModel.kt
package com.aryandi.movieapp.presentation.feature

@HiltViewModel
class YourViewModel @Inject constructor(
    private val yourUseCase: YourUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<YourUiState>(Loading)
    val uiState: StateFlow<YourUiState> = _uiState.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            yourUseCase().collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> YourUiState.Loading
                    is Result.Success -> YourUiState.Success(result.data)
                    is Result.Error -> YourUiState.Error(result.exception.message)
                }
            }
        }
    }
}

sealed class YourUiState {
    object Loading : YourUiState()
    data class Success(val data: Data) : YourUiState()
    data class Error(val message: String) : YourUiState()
}
```

### 8. Create Screen

```kotlin
// app/presentation/feature/YourScreen.kt
package com.aryandi.movieapp.presentation.feature

@Composable
fun YourScreen(
    viewModel: YourViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (val state = uiState) {
        is YourUiState.Loading -> LoadingIndicator()
        is YourUiState.Success -> YourContent(state.data)
        is YourUiState.Error -> ErrorMessage(
            message = state.message,
            onRetry = { viewModel.loadData() }
        )
    }
}
```

## 🎨 Common Patterns

### Result Wrapper Pattern

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

### UI State Pattern

```kotlin
sealed class UiState<out T> {
    object Initial : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

### Mapper Pattern

```kotlin
// Extension function approach
fun Dto.toDomain(): Entity = Entity(...)
fun Entity.toDto(): Dto = Dto(...)

// Or mapper object
object Mapper {
    fun dtoToDomain(dto: Dto): Entity = Entity(...)
}
```

## 🔍 Checklist: Adding New Feature

- [ ] **Step 1**: Create domain entity
- [ ] **Step 2**: Create use case
- [ ] **Step 3**: Update repository interface
- [ ] **Step 4**: Create DTO
- [ ] **Step 5**: Create mapper
- [ ] **Step 6**: Implement repository method
- [ ] **Step 7**: Create ViewModel & UI State
- [ ] **Step 8**: Create Screen
- [ ] **Step 9**: Add to navigation
- [ ] **Step 10**: Write tests

## 🚫 Common Mistakes to Avoid

### ❌ DON'T: Use Android classes in domain

```kotlin
// BAD
class UseCase(private val context: Context)
```

### ✅ DO: Keep domain pure Kotlin

```kotlin
// GOOD
class UseCase(private val repository: Repository)
```

### ❌ DON'T: Call repository from ViewModel

```kotlin
// BAD
class ViewModel(private val repository: Repository) {
    fun load() = repository.getData()
}
```

### ✅ DO: Use use cases

```kotlin
// GOOD
class ViewModel(private val useCase: GetDataUseCase) {
    fun load() = useCase()
}
```

### ❌ DON'T: Use DTOs in domain/presentation

```kotlin
// BAD
fun displayMovie(movie: MovieDto)
```

### ✅ DO: Use domain models

```kotlin
// GOOD
fun displayMovie(movie: Movie)
```

### ❌ DON'T: Put business logic in ViewModel

```kotlin
// BAD
class ViewModel {
    fun processData(data: List<Item>) {
        val filtered = data.filter { it.price > 100 } // Business logic!
        _state.value = Success(filtered)
    }
}
```

### ✅ DO: Put business logic in use case

```kotlin
// GOOD
class FilterExpensiveItemsUseCase {
    operator fun invoke(items: List<Item>): List<Item> {
        return items.filter { it.price > 100 }
    }
}
```

## 📝 Naming Conventions

| Component | Pattern | Example |
|-----------|---------|---------|
| Entity | Noun | `Movie`, `User`, `Product` |
| Use Case | Verb + Noun + UseCase | `GetMoviesUseCase`, `SaveUserUseCase` |
| Repository Interface | Noun + Repository | `MovieRepository`, `UserRepository` |
| Repository Impl | Interface + Impl | `MovieRepositoryImpl` |
| DTO | Noun + Dto | `MovieDto`, `UserDto` |
| Mapper | Noun + Mapper | `MovieMapper`, `UserMapper` |
| ViewModel | Screen + ViewModel | `MoviesViewModel`, `ProfileViewModel` |
| Screen | Screen + Screen | `MoviesScreen`, `ProfileScreen` |
| UI State | Screen + UiState | `MoviesUiState`, `ProfileUiState` |

## 🧪 Testing Quick Reference

### Test Domain Layer (Pure Kotlin)

```kotlin
class GetMoviesUseCaseTest {
    @Test
    fun `should return movies from repository`() {
        val mockRepo = mock<MovieRepository>()
        val useCase = GetMoviesUseCase(mockRepo)
        
        // Test pure business logic
    }
}
```

### Test ViewModel

```kotlin
class MoviesViewModelTest {
    @Test
    fun `should emit success state when data loads`() {
        val mockUseCase = mock<GetMoviesUseCase>()
        val viewModel = MoviesViewModel(mockUseCase)
        
        // Test state management
    }
}
```

### Test Repository

```kotlin
class MovieRepositoryTest {
    @Test
    fun `should map dto to domain model`() {
        val mockApi = mock<MovieApiService>()
        val repository = MovieRepositoryImpl(mockApi)
        
        // Test data transformation
    }
}
```

## 🔧 Dependency Injection (Hilt)

### Provide Use Case

```kotlin
@Provides
@Singleton
fun provideYourUseCase(
    repository: YourRepository
): YourUseCase = YourUseCase(repository)
```

### Provide Repository

```kotlin
@Provides
@Singleton
fun provideYourRepository(
    apiService: YourApiService
): YourRepository = YourRepositoryImpl(apiService)
```

### Provide API Service

```kotlin
@Provides
@Singleton
fun provideYourApiService(
    retrofit: Retrofit
): YourApiService = retrofit.create(YourApiService::class.java)
```

## 📊 Decision Tree

### "Where should this code go?"

```
Is it business logic?
├─ YES → Domain (Use Case)
└─ NO
   │
   Is it UI state management?
   ├─ YES → Presentation (ViewModel)
   └─ NO
      │
      Is it UI rendering?
      ├─ YES → UI (Composable)
      └─ NO
         │
         Is it data fetching/storage?
         ├─ YES → Data (Repository)
         └─ NO → Core (Utility)
```

## 🎯 Remember

1. **Domain = Business Rules** (What)
2. **Data = Implementation** (How)
3. **Presentation = State** (When)
4. **UI = Display** (Show)

## 🚀 Quick Commands

```bash
# Build project
./gradlew build

# Run tests
./gradlew test

# Run specific module tests
./gradlew :domain:test

# Install debug
./gradlew installDebug

# Clean
./gradlew clean
```

---

**Keep this handy!** 📌
