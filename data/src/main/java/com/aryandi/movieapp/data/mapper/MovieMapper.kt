package com.aryandi.movieapp.data.mapper

import com.aryandi.movieapp.data.base.DataToDomainMapper
import com.aryandi.movieapp.data.remote.dto.MovieDto
import com.aryandi.movieapp.domain.model.Movie

/**
 * Mapper to convert between data layer DTOs and domain models
 * Implements DataToDomainMapper for type safety
 *
 * Following the Mapper pattern:
 * - Keeps layers independent
 * - Single responsibility (only mapping)
 * - Easy to test
 */
object MovieMapper : DataToDomainMapper<MovieDto, Movie> {

    /**
     * Convert MovieDto to domain Movie model
     * Implements the interface contract
     */
    override fun toDomain(dataModel: MovieDto): Movie {
        return Movie(
            id = dataModel.id,
            title = dataModel.title,
            overview = dataModel.overview,
            posterPath = dataModel.posterPath,
            backdropPath = dataModel.backdropPath,
            releaseDate = dataModel.releaseDate,
            voteAverage = dataModel.voteAverage,
            voteCount = dataModel.voteCount
        )
    }

    /**
     * Convert list of MovieDto to list of domain Movie models
     * Extension function for convenience
     */
    fun List<MovieDto>.toDomain(): List<Movie> {
        return map { toDomain(it) }
    }
}
