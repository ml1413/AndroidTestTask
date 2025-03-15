package app.bettermetesttask.domainmovies.entries

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val posterPath: String?,
    val liked: Boolean = false
)
// не смог подключить  для передачи аргумента в другой фрагмент :( parcelize !!!!!!!!!!!!!!!!!!!
object Arg { var movie: Movie? = null }