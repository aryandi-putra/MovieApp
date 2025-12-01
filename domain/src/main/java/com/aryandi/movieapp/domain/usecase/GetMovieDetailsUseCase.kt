package com.aryandi.movieapp.domain.usecase

import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.repository.MovieRepository
import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting movie details
 * Extends BaseUseCase with Int parameter (movie ID)
 *
 * Example of Single Responsibility Principle:
 * - One use case = one business operation
 * - Only fetches movie details
 */
class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository
) : BaseUseCase<Int, Movie>() {

    /**
     * Execute the use case
     * @param params The ID of the movie to fetch
     * @return Flow of Result containing movie details
     */
    override fun execute(params: Int): Flow<Result<Movie>> {
        return movieRepository.getMovieDetails(params)
    }
}
