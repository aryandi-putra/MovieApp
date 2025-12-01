package com.aryandi.movieapp.data.repository

import com.aryandi.movieapp.data.base.BaseRepository
import com.aryandi.movieapp.data.mapper.MovieMapper
import com.aryandi.movieapp.data.remote.api.MovieApiService
import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.repository.MovieRepository
import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of MovieRepository
 * Extends BaseRepository for common data access patterns
 *
 * Following Clean Architecture:
 * - Implements domain interface
 * - Depends on abstractions (MovieRepository interface)
 * - Independent of UI layer
 */
class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val apiKey: String
) : BaseRepository(), MovieRepository {

    /**
     * Get popular movies from API
     * Uses BaseRepository's safeApiCall for automatic error handling
     */
    override fun getPopularMovies(): Flow<Result<List<Movie>>> {
        return safeApiCall {
            val response = apiService.getPopularMovies(apiKey)
            response.results.map { MovieMapper.toDomain(it) }
        }
    }

    /**
     * Get movie details by ID
     * Uses BaseRepository's safeApiCall for automatic error handling
     */
    override fun getMovieDetails(movieId: Int): Flow<Result<Movie>> {
        return safeApiCall {
            val movieDto = apiService.getMovieDetails(movieId, apiKey)
            MovieMapper.toDomain(movieDto)
        }
    }

    /**
     * Search movies by query
     * Uses BaseRepository's safeApiCall for automatic error handling
     */
    override fun searchMovies(query: String): Flow<Result<List<Movie>>> {
        return safeApiCall {
            val response = apiService.searchMovies(apiKey, query)
            response.results.map { MovieMapper.toDomain(it) }
        }
    }
}
