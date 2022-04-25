package org.rowanhamwood.hungr.ui.recipelist

import org.rowanhamwood.hungr.HungrApplication
import android.net.Uri
import android.os.Bundle
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
import org.rowanhamwood.hungr.viewmodel.RecipeViewModelFactory


class RecipeListFragment : Fragment() {



    private var _binding: FragmentRecipeListBinding? = null
    private val sharedViewModel by activityViewModels<RecipeViewModel>() {
        RecipeViewModelFactory((requireContext().applicationContext as HungrApplication).recipesRepository,
            (requireContext().applicationContext as HungrApplication).sharedPreferences)
    }


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
        val recyclerView = binding.recyclerview
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))

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


        val swipeHandler = ItemSwipeHandler(requireContext(), sharedViewModel)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)



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