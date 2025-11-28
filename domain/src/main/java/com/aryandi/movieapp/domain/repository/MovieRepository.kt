package com.aryandi.movieapp.domain.repository

import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining data operations
 * This abstraction allows the domain layer to be independent of data sources
 */
interface MovieRepository {

    /**
     * Get a list of popular movies
     * @return Flow of Result containing list of movies
     */
    fun getPopularMovies(): Flow<Result<List<Movie>>>

    /**
     * Get movie details by ID
     * @param movieId The ID of the movie
     * @return Flow of Result containing movie details
     */
    fun getMovieDetails(movieId: Int): Flow<Result<Movie>>

    /**
     * Search movies by query
     * @param query Search query string
     * @return Flow of Result containing list of matching movies
     */
    fun searchMovies(query: String): Flow<Result<List<Movie>>>
}
