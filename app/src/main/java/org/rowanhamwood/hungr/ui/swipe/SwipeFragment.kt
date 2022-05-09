package org.rowanhamwood.hungr.ui.swipe

import org.rowanhamwood.hungr.HungrApplication
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import org.rowanhamwood.hungr.R

import org.rowanhamwood.hungr.databinding.FragmentSwipeBinding
import org.rowanhamwood.hungr.viewmodel.RecipeViewModel
import org.rowanhamwood.hungr.viewmodel.RecipeViewModelFactory

private const val TAG = "SwipeFragment"
private const val TOP_CARD = "TOP_CARD"
private const val GET_NEXT = "GET_NEXT"

class SwipeFragment : Fragment(), CardStackListener {


    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: SwipeAdapter
    private lateinit var cardStackView: CardStackView
    private lateinit var sharedPreferences : SharedPreferences



    private val sharedViewModel by activityViewModels<RecipeViewModel>() {
        RecipeViewModelFactory((requireContext().applicationContext as HungrApplication).recipesRepository,
            (requireContext().applicationContext as HungrApplication).sharedPreferences)
    }





    private var _binding: FragmentSwipeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        _binding = FragmentSwipeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        Log.d(TAG, "onCreateView: ${sharedViewModel.recipes.value}")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        _binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            swipeFragment = this@SwipeFragment
        }

        sharedPreferences = requireContext().getSharedPreferences(getString(R.string.preference_file_key),  Context.MODE_PRIVATE)

        adapter = SwipeAdapter()
        cardStackView = binding.cardStackView
        Log.d(TAG, "onViewCreated: cardstackview $cardStackView")
        manager = CardStackLayoutManager(requireContext(), this)
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
//        cardStackView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))


        val cardPosition = sharedPreferences.getInt(TOP_CARD, 0)
        Log.d(TAG, "onViewCreated: card position $cardPosition")
        manager.topPosition = cardPosition

        val errorTextView = binding.swipeErrorText
        val errorImageView = binding.swipeErrorImage

        Log.d(TAG, "onViewCreated: recipes value ${sharedViewModel.recipes.value}")

//        if (sharedViewModel.recipes.value.isNullOrEmpty()){
//            cardStackView.visibility = View.GONE
//            errorTextView.visibility = View.VISIBLE
//            errorImageView.visibility = View.VISIBLE
//
//
//        } else{
//            cardStackView.visibility = View.VISIBLE
//            errorTextView.visibility = View.GONE
//            errorImageView.visibility = View.GONE
//        }

            errorTextView.visibility = View.GONE
            errorImageView.visibility = View.GONE






    }




    override fun onDestroyView() {
        sharedPreferences.edit().putInt(TOP_CARD, manager.topPosition).apply()
        super.onDestroyView()
        _binding = null

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.d(TAG, "onCardDragging: starts")
    }

    override fun onCardSwiped(direction: Direction?) {
        if (manager.topPosition == adapter.itemCount) {
            sharedViewModel.getRecipeData(true, false)
            sharedPreferences.edit().putBoolean(GET_NEXT, true).apply()
        }
        val item = manager.topPosition -1
        sharedPreferences.edit().putInt(TOP_CARD, manager.topPosition).apply()


        if (direction == Direction.Right){

            Log.d(TAG, "onCardSwiped: $item")
            val recipe =  sharedViewModel.recipes.value?.get(item)

            if (recipe!= null) {
                sharedViewModel.setFavouriteRecipes(recipe)

            }

        }

    }

    override fun onCardRewound() {
        Log.d(TAG, "onCardRewound: starts")
    }

    override fun onCardCanceled() {
        Log.d(TAG, "onCardCanceled: starts")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.d(TAG, "onCardAppeared: starts")
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        Log.d(TAG, "onCardDisappeared: starts")
    }
}