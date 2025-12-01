package com.aryandi.movieapp.domain.usecase

import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

/**
 * Base class for all use cases in the domain layer
 * Provides common functionality for executing business logic
 *
 * @param Params Input parameters type for the use case
 * @param ReturnType Output type wrapped in Result
 */
abstract class BaseUseCase<in Params, ReturnType>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * Execute the use case with given parameters
     * Automatically handles threading and basic error handling
     */
    operator fun invoke(params: Params): Flow<Result<ReturnType>> {
        return execute(params)
            .catch { exception ->
                emit(Result.Error(exception))
            }
            .flowOn(coroutineDispatcher)
    }

    /**
     * Abstract method to be implemented by concrete use cases
     * Contains the actual business logic
     */
    protected abstract fun execute(params: Params): Flow<Result<ReturnType>>
}

/**
 * Base class for use cases that don't require parameters
 */
abstract class BaseUseCaseNoParams<ReturnType>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(): Flow<Result<ReturnType>> {
        return execute()
            .catch { exception ->
                emit(Result.Error(exception))
            }
            .flowOn(coroutineDispatcher)
    }

    protected abstract fun execute(): Flow<Result<ReturnType>>
}

/**
 * Base class for suspend use cases (single emission)
 */
abstract class BaseSuspendUseCase<in Params, ReturnType>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(params: Params): Result<ReturnType> {
        return try {
            execute(params)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    protected abstract suspend fun execute(params: Params): Result<ReturnType>
}
