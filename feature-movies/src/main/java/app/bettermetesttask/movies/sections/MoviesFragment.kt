package app.bettermetesttask.movies.sections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.bettermetesttask.domainmovies.entries.Arg
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import app.bettermetesttask.featurecommon.utils.views.gone
import app.bettermetesttask.featurecommon.utils.views.visible
import app.bettermetesttask.movies.R
import app.bettermetesttask.movies.databinding.MoviesFragmentBinding
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class MoviesFragment : Fragment(R.layout.movies_fragment), Injectable {

    @Inject
    lateinit var viewModelProvider: Provider<MoviesViewModel>

    @Inject
    lateinit var adapter: MoviesAdapter

    private lateinit var binding: MoviesFragmentBinding

    private val viewModel by viewModels<MoviesViewModel> {
        SimpleViewModelProviderFactory(
            viewModelProvider
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MoviesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.moviesStateFlow.collect(::renderMoviesState)
            }
        }
        adapter.onItemClicked = { movie ->
            Arg.movie = movie
            findNavController().navigate(R.id.action_moviesFragment_to_detailsFragment)
        }
        adapter.onItemLiked = { movie ->
            viewModel.likeMovie(movie)
        }
    }

    private fun renderMoviesState(state: MoviesState) {
        with(binding) {
            when (state) {
                MoviesState.Loading -> {
                    rvList.gone()
                    progressBar.visible()
                }

                is MoviesState.Loaded -> {
                    progressBar.gone()
                    rvList.visible()
                    adapter.apply {
                        rvList.adapter = this
                        submitList(state.movies)
                    }
                }

                is MoviesState.Error -> {
                    // no op
                    tvError.text = state.error
                    tvError.visible()
                    progressBar.gone()
                    rvList.gone()
                }

                MoviesState.Initial -> {}
            }
        }
    }
}