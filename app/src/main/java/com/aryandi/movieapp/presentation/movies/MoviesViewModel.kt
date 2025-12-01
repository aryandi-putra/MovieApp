package com.aryandi.movieapp.presentation.movies

import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.usecase.GetPopularMoviesUseCase
import com.aryandi.movieapp.domain.common.Result
import com.aryandi.movieapp.presentation.base.BaseViewModel
import com.aryandi.movieapp.presentation.base.UiEvent
import com.aryandi.movieapp.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for Movies screen
 * Extends BaseViewModel for common functionality
 *
 * Following MVVM pattern:
 * - ViewModel is part of Presentation layer
 * - Manages UI state
 * - Executes use cases from domain layer
 * - No Android framework dependencies
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : BaseViewModel<MoviesUiState, MoviesUiEvent>() {

    /**
     * Provide initial state
     */
    override fun initialState(): MoviesUiState = MoviesUiState.Loading

    init {
        loadMovies()
    }

    /**
     * Load popular movies
     * Uses BaseViewModel's launchWithHandler for automatic error handling
     */
    fun loadMovies() {
        launchWithHandler {
            getPopularMoviesUseCase().collect { result ->
                setState {
                    when (result) {
                        is Result.Loading -> MoviesUiState.Loading
                        is Result.Success -> {
                            if (result.data.isEmpty()) {
                                MoviesUiState.Empty
                            } else {
                                MoviesUiState.Success(result.data)
                            }
                        }

                        is Result.Error -> MoviesUiState.Error(
                            result.exception.message ?: "An error occurred"
                        )
                    }
                }
            }
        }
    }

    /**
     * Handle movie click - sends navigation event
     */
    fun onMovieClick(movieId: Int) {
        sendEvent(MoviesUiEvent.NavigateToDetails(movieId))
    }

    /**
     * Handle error - can show toast or retry
     */
    override fun handleError(throwable: Throwable) {
        super.handleError(throwable)
        setState { MoviesUiState.Error(throwable.message ?: "Unknown error") }
    }
}

/**
 * UI State for Movies screen
 * Sealed class represents all possible states
 * Implements UiState marker interface
 */
sealed class MoviesUiState : UiState {
    object Loading : MoviesUiState()
    data class Success(val movies: List<Movie>) : MoviesUiState()
    data class Error(val message: String) : MoviesUiState()
    object Empty : MoviesUiState()
}

/**
 * UI Events for Movies screen
 * Represents one-time events (navigation, show toast, etc.)
 * Implements UiEvent marker interface
 */
sealed class MoviesUiEvent : UiEvent {
    data class NavigateToDetails(val movieId: Int) : MoviesUiEvent()
    data class ShowToast(val message: String) : MoviesUiEvent()
}
