package com.aryandi.movieapp.data.mapper

import com.aryandi.movieapp.data.remote.dto.MovieDto
import com.aryandi.movieapp.domain.model.Movie

/**
 * Mapper to convert between data layer DTOs and domain models
 * Following the Mapper pattern to keep layers independent
 */
object MovieMapper {

    /**
     * Convert MovieDto to domain Movie model
     */
    fun MovieDto.toDomain(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }

    /**
     * Convert list of MovieDto to list of domain Movie models
     */
    fun List<MovieDto>.toDomain(): List<Movie> {
        return map { it.toDomain() }
    }
}
