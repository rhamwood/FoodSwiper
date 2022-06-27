package org.rowanhamwood.hungr

import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

import coil.load
import com.yuyakaido.android.cardstackview.CardStackView
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.ui.recipelist.RecipeListAdapter
import org.rowanhamwood.hungr.ui.swipe.SwipeAdapter
import org.rowanhamwood.hungr.utils.wrapEspressoIdlingResource
import java.io.File

private const val TAG = "BindingAdapters"

@BindingAdapter("recipeImageUrl")
fun bindRecipeImage(imgView: ImageView, imgUrl: String) {
    Log.d(TAG, "bindRecipeImage: $imgUrl")
    wrapEspressoIdlingResource {
        imgUrl.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            Log.d(TAG, "bindRecipeImage: $imgUri")
            imgView.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)

            }
        }
    }
}

@BindingAdapter("favouriteImageUrl")
fun bindFavImage(imgView: ImageView, imgFilePath: String) {
    Log.d(TAG, "bindFavImage: $imgFilePath")

    imgFilePath.let {
        val imgFile = File(imgFilePath)
        imgView.load(imgFile) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listRecipes")
fun bindCardStackView(
    cardStackView: CardStackView,
    data: List<RecipeModel>?
) { wrapEspressoIdlingResource {
    val adapter = cardStackView.adapter as SwipeAdapter
    adapter.submitList(data)
}

}

@BindingAdapter("listFavouriteRecipes")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<DatabaseRecipe>?
) {
    val adapter = recyclerView.adapter as RecipeListAdapter
    adapter.submitList(data)

}


