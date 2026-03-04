app
core
core-network
core-database
feature-homeandroid-clean-architecture-template
│
├── app/
├── core/
├── core-network/
├── core-database/
├── feature-home/
│   ├── data/
│   ├── domain/
│   └── presentation/data class User(
    val id: Int,
    val name: String,
    val email: String
)interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
}sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = api.getUsers()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val state: StateFlow<UiState<List<User>>> = _state

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            when (val result = repository.getUsers()) {
                is Result.Success -> _state.value = UiState.Success(result.data)
                is Result.Error -> _state.value =
                    UiState.Error(result.exception.message ?: "Unknown error")
            }
        }
    }
}# Android Clean Architecture Template

Production-ready multi-module Android template built with:

- Kotlin
- Jetpack Compose
- Clean Architecture
- MVVM
- Hilt
- Retrofit
- Room
- Coroutines & Flow

---

## Architecture Overview

This project follows a strict separation of concerns:

- Data Layer
- Domain Layer
- Presentation Layer

Each feature is isolated into its own module for scalability.

---

## Why This Architecture?

- Testable
- Scalable
- Maintainable
- Production-ready
