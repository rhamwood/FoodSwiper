package org.rowanhamwood.hungr

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load

import com.yuyakaido.android.cardstackview.CardStackView
import org.rowanhamwood.hungr.network.Recipe
import org.rowanhamwood.hungr.ui.swipe.SwipeAdapter

private const val IMG_BASE_URL = "https://spoonacular.com/recipeImages/"
private const val IMG_SIZE_SMALL = "312x231"
private const val IMG_SIZE_MED = "480x360"
private const val IMG_SIZE_LARGE = "636x393"


@BindingAdapter("imageId","imageType" )
fun bindImage(imgView: ImageView, imgId: Int?,  imgType: String? ) {
    val sizedImgUrl = "$IMG_BASE_URL$imgId-$IMG_SIZE_LARGE.$imgType"
    sizedImgUrl?.let {
        val imgUri = sizedImgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listData")
fun bindCardStackView(cardStackView: CardStackView,
                     data: List<Recipe>?) {
    val adapter = cardStackView.adapter as SwipeAdapter
    adapter.submitList(data)

}