package com.aryandi.movieapp.domain.usecase

import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.repository.MovieRepository
import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting popular movies
 * Extends BaseUseCaseNoParams since it doesn't require parameters
 *
 * Example of Clean Architecture:
 * - Pure business logic
 * - Framework independent
 * - Testable
 */
class GetPopularMoviesUseCase(
    private val movieRepository: MovieRepository
) : BaseUseCaseNoParams<List<Movie>>() {

    /**
     * Execute the use case
     * @return Flow of Result containing list of popular movies
     */
    override fun execute(): Flow<Result<List<Movie>>> {
        return movieRepository.getPopularMovies()
    }
}
