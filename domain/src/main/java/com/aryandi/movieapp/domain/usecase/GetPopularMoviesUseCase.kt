package com.aryandi.movieapp.domain.usecase

import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.repository.MovieRepository
import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting popular movies
 * Encapsulates business logic for fetching popular movies
 *
 * Following Single Responsibility Principle - this use case has only one reason to change:
 * if the business rules for fetching popular movies change
 */
class GetPopularMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    /**
     * Execute the use case
     * @return Flow of Result containing list of popular movies
     */
    operator fun invoke(): Flow<Result<List<Movie>>> {
        return movieRepository.getPopularMovies()
    }
}
