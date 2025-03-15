package app.bettermetesttask.movies.sections

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.interactors.AddMovieToFavoritesUseCase
import app.bettermetesttask.domainmovies.interactors.GetLocalUseCase
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
    private val getLocalUseCase: GetLocalUseCase

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
                    when (result) {
                        is Result.Error -> moviesMutableFlow.emit(MoviesState.Error(result.error.message.toString()))
                        is Result.Success -> moviesMutableFlow.emit(MoviesState.Loaded(result.data))
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

    fun getLocalMovies() {
        viewModelScope.launch {
            // todo need emit
            getLocalUseCase.invoke()
        }
    }


}