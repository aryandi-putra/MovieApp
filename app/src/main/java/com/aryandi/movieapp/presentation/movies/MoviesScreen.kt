package com.aryandi.movieapp.presentation.movies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aryandi.movieapp.core.ui.components.ErrorMessage
import com.aryandi.movieapp.core.ui.components.LoadingIndicator
import com.aryandi.movieapp.core.ui.components.MovieCard

/**
 * Movies screen - UI layer
 * Pure Composable function that observes ViewModel state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Popular Movies") }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is MoviesUiState.Loading -> {
                LoadingIndicator()
            }

            is MoviesUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(state.movies) { movie ->
                        MovieCard(
                            title = movie.title,
                            posterPath = movie.posterPath,
                            rating = movie.voteAverage,
                            onClick = { onMovieClick(movie.id) }
                        )
                    }
                }
            }

            is MoviesUiState.Error -> {
                ErrorMessage(
                    message = state.message,
                    onRetry = { viewModel.loadMovies() }
                )
            }

            MoviesUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        text = "No movies found",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
