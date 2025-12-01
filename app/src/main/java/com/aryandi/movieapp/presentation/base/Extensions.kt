package com.aryandi.movieapp.presentation.base

import com.aryandi.movieapp.core.base.ScreenState
import com.aryandi.movieapp.domain.common.Result

/**
 * Extension functions for converting between layers
 */

/**
 * Convert domain Result to ScreenState for UI
 */
fun <T> Result<T>.toScreenState(): ScreenState<T> {
    return when (this) {
        is Result.Loading -> ScreenState.Loading
        is Result.Success -> {
            // Handle empty list case
            if (this.data is List<*> && (this.data as List<*>).isEmpty()) {
                ScreenState.Empty()
            } else {
                ScreenState.Success(this.data)
            }
        }

        is Result.Error -> {
            ScreenState.Error(this.exception.message ?: "An error occurred")
        }
    }
}

/**
 * Convert Result to UI state message
 */
fun <T> Result<T>.toMessage(): String? {
    return when (this) {
        is Result.Error -> exception.message
        else -> null
    }
}

/**
 * Check if Result is successful
 */
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success

/**
 * Check if Result is error
 */
fun <T> Result<T>.isError(): Boolean = this is Result.Error

/**
 * Check if Result is loading
 */
fun <T> Result<T>.isLoading(): Boolean = this is Result.Loading

/**
 * Get data or null
 */
fun <T> Result<T>.dataOrNull(): T? {
    return when (this) {
        is Result.Success -> data
        else -> null
    }
}
