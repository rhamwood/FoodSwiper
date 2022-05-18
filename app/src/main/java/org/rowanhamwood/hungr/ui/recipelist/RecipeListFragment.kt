package org.rowanhamwood.hungr.ui.recipelist

import org.rowanhamwood.hungr.HungrApplication
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.rowanhamwood.hungr.R
import org.rowanhamwood.hungr.databinding.FragmentRecipeListBinding
import org.rowanhamwood.hungr.viewmodel.RecipeViewModel
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.rowanhamwood.hungr.ResultState


@AndroidEntryPoint
class RecipeListFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentRecipeListBinding? = null
    private val sharedViewModel by activityViewModels<RecipeViewModel>()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner

        }



        //Initialize recyclerView and layoutManager
        recyclerView = binding.recyclerview
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        //Initialize adapter and onclickListener with Custom Tabs Intent
        val adapter =
            RecipeListAdapter(RecipeListAdapter.RecipeListListener { recipeUrl ->
                sharedViewModel.setUrl(recipeUrl)

                val builder = CustomTabsIntent.Builder()
                val defaultColors = CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
                    .build()
                builder.setDefaultColorSchemeParams(defaultColors)
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(requireContext(), Uri.parse(recipeUrl))
            })

        recyclerView.adapter = adapter


        //Initialize itemTouchHelper for swipe to delete
        val swipeHandler = ItemSwipeHandler(requireContext(), sharedViewModel)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)


        // error screen setup
        val errorTextView = binding.recipeListErrorTextView
        val errorImageView = binding.recipeListErrorImageView
        val loadingImage = binding.recipeListLoadingImage

        errorTextView.visibility = View.GONE
        errorImageView.visibility = View.GONE
        loadingImage.visibility = View.GONE

        //uiState listener for favourite recipe data
        sharedViewModel.favRecipeUiState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is ResultState.Loading -> {
                    loadingImage.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                    errorImageView.visibility = View.GONE


                }

                is ResultState.Success -> { /* show success in UI */
                    recyclerView.visibility = View.VISIBLE
                    errorTextView.visibility = View.GONE
                    errorImageView.visibility = View.GONE
                    loadingImage.visibility = View.GONE


                }

                is ResultState.Failure -> { /* show error in UI with state.message variable */
                    errorTextView.text = state.message
                    recyclerView.visibility = View.GONE
                    loadingImage.visibility = View.GONE
                    errorTextView.visibility = View.VISIBLE
                    errorImageView.visibility = View.VISIBLE


                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}