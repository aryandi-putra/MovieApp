package com.aryandi.movieapp.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel providing common functionality for all ViewModels
 * Follows MVVM pattern with state management and event handling
 *
 * @param State UI state type (sealed class recommended)
 */
abstract class BaseViewModel<State : UiState, Event : UiEvent> : ViewModel() {

    // State management
    private val _uiState: MutableStateFlow<State> by lazy {
        MutableStateFlow(initialState())
    }
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    // One-time events (navigation, show toast, etc.)
    private val _uiEvent = MutableSharedFlow<Event>()
    val uiEvent: SharedFlow<Event> = _uiEvent.asSharedFlow()

    // Error handling
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    /**
     * Provides the initial state for the UI
     */
    protected abstract fun initialState(): State

    /**
     * Updates the UI state
     */
    protected fun setState(reducer: State.() -> State) {
        _uiState.value = _uiState.value.reducer()
    }

    /**
     * Gets current state value
     */
    protected fun currentState(): State = _uiState.value

    /**
     * Sends a one-time UI event
     */
    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    /**
     * Launch coroutine with error handling
     */
    protected fun launchWithHandler(
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(exceptionHandler) {
            block()
        }
    }

    /**
     * Handle errors (can be overridden for custom error handling)
     */
    protected open fun handleError(throwable: Throwable) {
        // Default error handling - log or update error state
        throwable.printStackTrace()
    }
}

/**
 * Marker interface for UI State
 * All UI states should implement this
 */
interface UiState

/**
 * Marker interface for UI Events
 * All one-time UI events should implement this
 */
interface UiEvent

/**
 * Base loading state that can be mixed into any UI state
 */
interface LoadingState {
    val isLoading: Boolean
}

/**
 * Base error state that can be mixed into any UI state
 */
interface ErrorState {
    val error: String?
}

/**
 * Common UI state combining loading and error
 */
sealed class BaseUiState : UiState {
    object Initial : BaseUiState()
    object Loading : BaseUiState()
    data class Error(val message: String) : BaseUiState()
}
