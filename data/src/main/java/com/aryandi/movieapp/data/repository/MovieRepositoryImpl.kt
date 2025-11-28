package com.aryandi.movieapp.data.repository

import com.aryandi.movieapp.data.remote.api.MovieApiService
import com.aryandi.movieapp.data.mapper.MovieMapper.toDomain
import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.repository.MovieRepository
import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of MovieRepository
 * This is where we implement the actual data fetching logic
 * Following the Repository pattern to abstract data sources
 */
class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val apiKey: String
) : MovieRepository {

    override fun getPopularMovies(): Flow<Result<List<Movie>>> = flow {
        try {
            emit(Result.Loading)
            val response = apiService.getPopularMovies(apiKey)
            val movies = response.results.toDomain()
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getMovieDetails(movieId: Int): Flow<Result<Movie>> = flow {
        try {
            emit(Result.Loading)
            val movieDto = apiService.getMovieDetails(movieId, apiKey)
            val movie = movieDto.toDomain()
            emit(Result.Success(movie))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun searchMovies(query: String): Flow<Result<List<Movie>>> = flow {
        try {
            emit(Result.Loading)
            val response = apiService.searchMovies(apiKey, query)
            val movies = response.results.toDomain()
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
