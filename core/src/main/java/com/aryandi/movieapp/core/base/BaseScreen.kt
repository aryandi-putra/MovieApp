package com.aryandi.movieapp.core.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.aryandi.movieapp.core.ui.components.ErrorMessage
import com.aryandi.movieapp.core.ui.components.LoadingIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Base composable that handles common UI patterns
 * - Loading states
 * - Error states
 * - Success content
 */
@Composable
fun <T> BaseScreen(
    state: ScreenState<T>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is ScreenState.Loading -> {
                LoadingIndicator()
            }

            is ScreenState.Success -> {
                content(state.data)
            }

            is ScreenState.Error -> {
                ErrorMessage(
                    message = state.message,
                    onRetry = onRetry
                )
            }

            is ScreenState.Empty -> {
                EmptyState(message = state.message)
            }
        }
    }
}

/**
 * Generic screen state for compose screens
 */
sealed class ScreenState<out T> {
    object Loading : ScreenState<Nothing>()
    data class Success<T>(val data: T) : ScreenState<T>()
    data class Error(val message: String) : ScreenState<Nothing>()
    data class Empty(val message: String = "No data available") : ScreenState<Nothing>()
}

/**
 * Empty state component
 */
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = message,
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Extension to collect UI events (one-time events like navigation)
 */
@Composable
fun <T> ObserveEvents(
    events: Flow<T>,
    onEvent: (T) -> Unit
) {
    LaunchedEffect(Unit) {
        events.collectLatest { event ->
            onEvent(event)
        }
    }
}

/**
 * Common screen result for handling different states
 */
sealed class ScreenResult<out T> {
    data class Success<T>(val data: T) : ScreenResult<T>()
    data class Error(val exception: Throwable) : ScreenResult<Nothing>()
    object Loading : ScreenResult<Nothing>()
}
