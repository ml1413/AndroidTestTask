package app.bettermetesttask.movies.sections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.bettermetesttask.domainmovies.entries.Arg
import app.bettermetesttask.featurecommon.utils.images.GlideApp
import app.bettermetesttask.movies.databinding.FragmentDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DetailsFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val movie by lazy { Arg.movie }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movie?.let { movieNotNull ->
            with(binding) {
                GlideApp.with(requireContext()).load(movieNotNull.posterPath).into(image)
                tv.text = movieNotNull.title
                tvInfo.text = movieNotNull.description
            }
        }
    }
}