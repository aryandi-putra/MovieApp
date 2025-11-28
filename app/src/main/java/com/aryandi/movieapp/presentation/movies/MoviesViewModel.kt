package com.aryandi.movieapp.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.usecase.GetPopularMoviesUseCase
import com.aryandi.movieapp.domain.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Movies screen
 * Following MVVM pattern - ViewModel is part of Presentation layer
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
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
                    is Result.Error -> MoviesUiState.Error(
                        result.exception.message ?: "An error occurred"
                    )
                }
            }
        }
    }
}

/**
 * UI State for Movies screen
 * Sealed class represents all possible states
 */
sealed class MoviesUiState {
    object Loading : MoviesUiState()
    data class Success(val movies: List<Movie>) : MoviesUiState()
    data class Error(val message: String) : MoviesUiState()
}
