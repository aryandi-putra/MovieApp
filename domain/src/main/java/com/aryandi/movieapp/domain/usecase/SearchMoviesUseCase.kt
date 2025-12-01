package com.aryandi.movieapp.domain.usecase

import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.repository.MovieRepository
import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for searching movies
 * Extends BaseUseCase with String parameter (search query)
 *
 * Demonstrates business logic in use case:
 * - Validates query length before calling repository
 */
class SearchMoviesUseCase(
    private val movieRepository: MovieRepository
) : BaseUseCase<String, List<Movie>>() {

    /**
     * Execute the use case
     * @param params The search query
     * @return Flow of Result containing list of matching movies
     */
    override fun execute(params: String): Flow<Result<List<Movie>>> {
        // Business rule: don't search if query is too short
        if (params.length < MIN_QUERY_LENGTH) {
            return flow { emit(Result.Success(emptyList())) }
        }

        return movieRepository.searchMovies(params)
    }

    companion object {
        private const val MIN_QUERY_LENGTH = 3
    }
}
