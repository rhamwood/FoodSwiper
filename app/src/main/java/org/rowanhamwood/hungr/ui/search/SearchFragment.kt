package org.rowanhamwood.hungr.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.rowanhamwood.hungr.R
import org.rowanhamwood.hungr.databinding.FragmentSearchBinding
import org.rowanhamwood.hungr.viewmodel.RecipeViewModel

private const val TAG = "SearchFragment"

class SearchFragment : Fragment() {


    private var _binding: FragmentSearchBinding? = null

    private val sharedViewModel: RecipeViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // search bar
        val searchView = binding.searchView
        val submitButton = binding.submitButton
        // cuisine selection menu
        val cuisineMenu = binding.cuisineMenu
        val cuisineTextView = binding.cuisineTextView
        // diet selection menu
        val healthMenu = binding.dietMenu
        val healthTextView = binding.dietTextView


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
            "None"

        )
        val healthAdapter = ArrayAdapter(requireContext(), R.layout.health_list_item, healthItems)
        (healthMenu.editText as? AutoCompleteTextView)?.setAdapter(healthAdapter)
        healthTextView.setText(sharedViewModel.health.value, false)

        healthTextView.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            if ((view as TextView).text == "None") {
                sharedViewModel.setHealth(null)
            } else {
                sharedViewModel.setHealth((view).text.toString())
            }
        }





        submitButton.setOnClickListener { view ->
            Log.d(TAG, "onCreateView: submit button clicked")
            if (searchView.query != "" && searchView.query.length > 0) {
                sharedViewModel.setSearch(searchView.query.toString())
                sharedViewModel.getRecipeData()
                goToNextScreen()

                Log.d(TAG, "onCreateView: search completed")
            } else {
                Toast.makeText(
                    requireContext(),
                    "please fill in the search field",
                    Toast.LENGTH_SHORT
                ).show()
            }
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


