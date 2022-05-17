package org.rowanhamwood.hungr.ui.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.rowanhamwood.hungr.HungrApplication
import org.rowanhamwood.hungr.R
import org.rowanhamwood.hungr.databinding.FragmentSearchBinding
import org.rowanhamwood.hungr.viewmodel.RecipeViewModel
import org.rowanhamwood.hungr.viewmodel.RecipeViewModelFactory

private const val TAG = "SearchFragment"
private const val CURRENT_SEARCH = "CURRENT_SEARCH"
private const val GET_NEXT = "GET_NEXT"
private const val TOP_CARD = "TOP_CARD"

class SearchFragment : Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private lateinit var sharedPreferences: SharedPreferences


//    private val sharedViewModel: RecipeViewModel by activityViewModels()

    private val sharedViewModel by activityViewModels<RecipeViewModel>() {
        RecipeViewModelFactory(
            (requireContext().applicationContext as HungrApplication).recipesRepository,
            (requireContext().applicationContext as HungrApplication).sharedPreferences
        )
    }


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
//        cuisineTextView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.teal_200))
        // diet selection menu
        val healthMenu = binding.dietMenu
        val healthTextView = binding.dietTextView
//        healthTextView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.teal_200))


        //cuisineMenu setup
        val cuisineItems = listOf(
            "None",
            "American",
            "Asian",
            "British",
            "Caribbean",
            "Central Europe",
            "Chinese",
            "Eastern Europe",
            "French",
            "Indian",
            "Italian",
            "Japanese",
            "Kosher",
            "Mediterranean",
            "Mexican",
            "Middle Eastern",
            "Nordic",
            "South American",
            "South East Asian"
        )
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

        //Diet menu setup
        val healthItems = listOf(
            "None",
            "Alcohol-Free",
            "Celery-Free",
            "Crustacean-Free",
            "Dairy-Free",
            "Egg-Free",
            "Fish-Free",
            "FODMAP-Free",
            "Gluten-Free",
            "Immuno-Supportive",
            "Keto-Friendly",
            "Kidney-Friendly",
            "Kosher",
            "Lupine-Free",
            "Mediterranean",
            "Mollusk-Free",
            "Mustard-Free",
            "Paleo",
            "Peanut-Free",
            "Pescatarian",
            "Pork-Free",
            "Red-Meat-Free",
            "Sesame-Free",
            "Shellfish-Free",
            "Soy-Free",
            "Sugar-Conscious",
            "Sulfite-Free",
            "Tree-Nut-Free",
            "Vegan",
            "Vegetarian",
            "Wheat-Free"


        )
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

        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("onQueryTextChange", "called");
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


                    Log.d(TAG, "onCreateView: search completed")


                }


                return true
            }
        })


        submitButton.setOnClickListener { view ->
            Log.d(TAG, "onCreateView: submit button clicked")
            if (searchView.query.isEmpty()) {
                Log.d(TAG, "onQueryTextSubmit: show search empty toast")
                Toast.makeText(
                    requireContext(),
                    "please fill in the search field",
                    Toast.LENGTH_SHORT
                ).show()
            }
            searchView.setQuery(searchView.query, true)

        }

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

    fun goToNextScreen() {
        findNavController().navigate(R.id.action_navigation_search_to_navigation_swipe)
    }


}


