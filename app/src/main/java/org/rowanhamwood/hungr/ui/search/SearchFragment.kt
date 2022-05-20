package org.rowanhamwood.hungr.ui.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.rowanhamwood.hungr.R
import org.rowanhamwood.hungr.databinding.FragmentSearchBinding
import org.rowanhamwood.hungr.viewmodel.RecipeViewModel

private const val TAG = "SearchFragment"
private const val CURRENT_SEARCH = "CURRENT_SEARCH"
private const val GET_NEXT = "GET_NEXT"
private const val TOP_CARD = "TOP_CARD"

@AndroidEntryPoint
class SearchFragment : Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedViewModel by activityViewModels<RecipeViewModel>()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root




        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        //sharedPreference for last search on restart
        sharedPreferences = requireContext().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        // search bar
        val searchView = binding.searchView
        val submitButton = binding.submitButton
        submitButton.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_green_500
            )
        )
        // cuisine selection menu
        val cuisineMenu = binding.cuisineMenu
        val cuisineTextView = binding.cuisineTextView

        // diet selection menu
        val healthMenu = binding.dietMenu
        val healthTextView = binding.dietTextView

        //setup cuisine selection menu
        val cuisineAdapter =
            ArrayAdapter(requireContext(), R.layout.cuisine_list_item, cuisineItems)
        (cuisineMenu.editText as? AutoCompleteTextView)?.setAdapter(cuisineAdapter)
        cuisineTextView.setText(sharedViewModel.cuisine.value, false)

        cuisineTextView.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            if ((view as TextView).text == "None") {
                sharedViewModel.setCuisine(null)
            } else {
                sharedViewModel.setCuisine((view).text.toString())
            }
        }

        //setup diet selection menu
        val healthAdapter = ArrayAdapter(requireContext(), R.layout.health_list_item, healthItems)
        (healthMenu.editText as? AutoCompleteTextView)?.setAdapter(healthAdapter)
        healthTextView.setText(sharedViewModel.health.value, false)

        healthTextView.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            if ((view as TextView).text == "None") {
                sharedViewModel.setHealth(null)
            } else {
                sharedViewModel.setHealth((view).text.toString().lowercase())
            }
        }

        //setup searchView
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                val searchQuery = query

                if (searchQuery != null) {

                    sharedViewModel.setSearch(searchQuery.toString())
                    sharedPreferences.edit().putString(CURRENT_SEARCH, searchQuery.toString())
                        .apply()
                    sharedViewModel.clearRecipes()
                    sharedPreferences.edit().putInt(TOP_CARD, 0).apply()
                    sharedViewModel.getRecipeData(false, false)
                    sharedPreferences.edit().putBoolean(GET_NEXT, false).apply()
                    sharedViewModel.setRecipesResultStateLoading()
                    goToNextScreen()
                }


                return true
            }
        })


        //setup submit button for search view
        submitButton.setOnClickListener { view ->
            if (searchView.query.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "please fill in the search field",
                    Toast.LENGTH_SHORT
                ).show()
            }
            searchView.setQuery(searchView.query, true)

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun goToNextScreen() {
        findNavController().navigate(R.id.action_navigation_search_to_navigation_swipe)
    }


}


