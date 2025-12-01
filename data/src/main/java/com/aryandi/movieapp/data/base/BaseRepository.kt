package com.aryandi.movieapp.data.base

import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Base repository providing common data access patterns
 * Implements common error handling and data transformation
 */
abstract class BaseRepository {

    /**
     * Safe API call that wraps the result in a Flow<Result<T>>
     * Automatically handles exceptions and loading states
     */
    protected fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Flow<Result<T>> = flow {
        emit(Result.Loading)
        try {
            val response = apiCall()
            emit(Result.Success(response))
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }

    /**
     * Execute API call with custom error handling
     */
    protected suspend fun <T> executeApiCall(
        call: suspend () -> T
    ): Result<T> {
        return try {
            Result.Success(call())
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    /**
     * Combine network and cache strategies
     * Network-first: Try network, fallback to cache
     */
    protected fun <T> networkFirst(
        fetchFromNetwork: suspend () -> T,
        fetchFromCache: suspend () -> T?,
        saveToCache: suspend (T) -> Unit
    ): Flow<Result<T>> = flow {
        emit(Result.Loading)

        try {
            // Try network first
            val networkResult = fetchFromNetwork()
            saveToCache(networkResult)
            emit(Result.Success(networkResult))
        } catch (networkException: Exception) {
            // Fallback to cache
            try {
                val cacheResult = fetchFromCache()
                if (cacheResult != null) {
                    emit(Result.Success(cacheResult))
                } else {
                    emit(Result.Error(networkException))
                }
            } catch (cacheException: Exception) {
                emit(Result.Error(networkException))
            }
        }
    }

    /**
     * Cache-first strategy
     * Show cache immediately, then fetch from network
     */
    protected fun <T> cacheFirst(
        fetchFromNetwork: suspend () -> T,
        fetchFromCache: suspend () -> T?,
        saveToCache: suspend (T) -> Unit
    ): Flow<Result<T>> = flow {
        emit(Result.Loading)

        // Try cache first
        val cacheResult = fetchFromCache()
        if (cacheResult != null) {
            emit(Result.Success(cacheResult))
        }

        // Then fetch from network
        try {
            val networkResult = fetchFromNetwork()
            saveToCache(networkResult)
            emit(Result.Success(networkResult))
        } catch (exception: Exception) {
            if (cacheResult == null) {
                emit(Result.Error(exception))
            }
            // If we already emitted cache, ignore network error
        }
    }
}

/**
 * Mapper interface for converting between data and domain models
 */
interface BaseMapper<DataModel, DomainModel> {
    fun toDomain(dataModel: DataModel): DomainModel
    fun toData(domainModel: DomainModel): DataModel
}

/**
 * One-way mapper (data to domain only)
 */
interface DataToDomainMapper<DataModel, DomainModel> {
    fun toDomain(dataModel: DataModel): DomainModel
}

/**
 * Extension for mapping lists
 */
fun <DataModel, DomainModel> List<DataModel>.toDomain(
    mapper: DataToDomainMapper<DataModel, DomainModel>
): List<DomainModel> {
    return map { mapper.toDomain(it) }
}
