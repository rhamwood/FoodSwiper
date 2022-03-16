package org.rowanhamwood.hungr.ui.recipelist

import android.app.PendingIntent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.rowanhamwood.hungr.R
import org.rowanhamwood.hungr.databinding.FragmentRecipeListBinding
import org.rowanhamwood.hungr.viewmodel.RecipeViewModel
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import org.rowanhamwood.hungr.ui.swipe.SwipeAdapter


class RecipeListFragment : Fragment() {



    private var _binding: FragmentRecipeListBinding? = null
    private val sharedViewModel: RecipeViewModel by activityViewModels()


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



//    fun goToNextScreen() {
////        findNavController().navigate(R.id.action_navigation_recipe_list_to_recipeDetailFragment)
//
//    }




}