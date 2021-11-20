package org.rowanhamwood.hungr.ui.swipe

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import org.rowanhamwood.hungr.R

import org.rowanhamwood.hungr.databinding.FragmentSwipeBinding
import org.rowanhamwood.hungr.viewmodel.RecipeViewModel
import java.util.*

private const val TAG = "SwipeFragment"
private const val TOP_CARD = "TOP_CARD"

class SwipeFragment : Fragment(), CardStackListener {

    private val manager by lazy { CardStackLayoutManager(requireContext(), this) }
    private val adapter by lazy { SwipeAdapter() }
    private val cardStackView by lazy { binding.cardStackView }
    private lateinit var sharedPreferences : SharedPreferences


    private val sharedViewModel: RecipeViewModel by activityViewModels()





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

        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        sharedPreferences = requireContext().getSharedPreferences(getString(R.string.preference_file_key),  Context.MODE_PRIVATE)
        val cardPosition = sharedPreferences.getInt(TOP_CARD, 0)
        manager.topPosition = cardPosition
        

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            swipeFragment = this@SwipeFragment
        }
        Log.d(TAG, "onViewCreated: $sharedViewModel")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.d(TAG, "onCardDragging: starts")
    }

    override fun onCardSwiped(direction: Direction?) {
        if (manager.topPosition == adapter.itemCount) {
            sharedViewModel.getNext()
        }
        if (direction == Direction.Right){
            val item = manager.topPosition -1
            Log.d(TAG, "onCardSwiped: $item")
            val recipe =  sharedViewModel.recipes.value?.get(item)

            if (recipe!= null) {
                sharedViewModel.setFavouriteRecipes(recipe)
                sharedPreferences.edit().putInt(TOP_CARD, manager.topPosition).apply()
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