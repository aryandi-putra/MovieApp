package com.aryandi.movieapp.domain.usecase

import com.aryandi.movieapp.domain.model.Movie
import com.aryandi.movieapp.domain.repository.MovieRepository
import com.aryandi.movieapp.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Use case for searching movies
 * Encapsulates business logic for searching movies by query
 */
class SearchMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    /**
     * Execute the use case
     * @param query The search query
     * @return Flow of Result containing list of matching movies
     */
    operator fun invoke(query: String): Flow<Result<List<Movie>>> {
        // Business rule: don't search if query is too short
        if (query.length < MIN_QUERY_LENGTH) {
            return flowOf(Result.Success(emptyList()))
        }

        return movieRepository.searchMovies(query)
    }

    companion object {
        private const val MIN_QUERY_LENGTH = 3
    }
}
