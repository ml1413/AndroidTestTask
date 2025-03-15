package app.bettermetesttask.domainmovies.interactors

import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import javax.inject.Inject

class GetLocalUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(): Result<List<Movie>> {
        // todo need refactor
        return repository.getLocalMovies()
    }
}