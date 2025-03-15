package app.bettermetesttask.movies.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.interactors.AddMovieToFavoritesUseCase
import app.bettermetesttask.domainmovies.interactors.ObserveMoviesUseCase
import app.bettermetesttask.domainmovies.interactors.RemoveMovieFromFavoritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val observeMoviesUseCase: ObserveMoviesUseCase,
    private val likeMovieUseCase: AddMovieToFavoritesUseCase,
    private val dislikeMovieUseCase: RemoveMovieFromFavoritesUseCase,

    ) : ViewModel() {

    private val moviesMutableFlow: MutableStateFlow<MoviesState> =
        MutableStateFlow(MoviesState.Loading)

    val moviesStateFlow: StateFlow<MoviesState> = moviesMutableFlow
        .onSubscription { loadMovies() }
        .stateIn(viewModelScope, SharingStarted.Lazily, MoviesState.Loading)

    private fun loadMovies() {
        viewModelScope.launch {
            observeMoviesUseCase()
                .collect { result ->
                    if (result is Result.Success) {
                        moviesMutableFlow.emit(MoviesState.Loaded(result.data))
                    }
                }
        }
    }

    fun likeMovie(movie: Movie) {
        with(movie.copy(liked = movie.liked.not())) {
            viewModelScope.launch {
                if (liked) {
                    likeMovieUseCase(id)
                } else {
                    dislikeMovieUseCase(id)
                }
            }
        }
    }


}