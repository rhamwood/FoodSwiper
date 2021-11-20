package org.rowanhamwood.hungr.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.rowanhamwood.hungr.databinding.FragmentRecipeDetailBinding
import org.rowanhamwood.hungr.viewmodel.RecipeViewModel


class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null

    private val sharedViewModel: RecipeViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val webView = binding.webView
        webView.settings.javaScriptEnabled
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
