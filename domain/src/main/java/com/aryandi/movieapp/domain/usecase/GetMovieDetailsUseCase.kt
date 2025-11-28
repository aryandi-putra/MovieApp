package com.aryandi.movieapp.domain.usecase

import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.repository.MovieRepository
import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting movie details
 * Encapsulates business logic for fetching detailed information about a specific movie
 */
class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository
) {
    /**
     * Execute the use case
     * @param movieId The ID of the movie to fetch
     * @return Flow of Result containing movie details
     */
    operator fun invoke(movieId: Int): Flow<Result<Movie>> {
        return movieRepository.getMovieDetails(movieId)
    }
}
