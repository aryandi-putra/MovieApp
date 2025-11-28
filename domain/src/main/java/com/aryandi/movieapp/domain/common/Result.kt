package com.aryandi.movieapp.domain.common

/**
 * A generic wrapper for operation results
 * Represents the outcome of an operation that can succeed or fail
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Extension function to check if result is successful
 */
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success

/**
 * Extension function to get data if successful, null otherwise
 */
fun <T> Result<T>.getOrNull(): T? = when (this) {
    is Result.Success -> data
    else -> null
}
