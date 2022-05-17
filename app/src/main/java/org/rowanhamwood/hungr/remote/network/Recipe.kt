package org.rowanhamwood.hungr.remote.network

import com.squareup.moshi.Json


data class Recipe(
    @Json(name = "recipe") val recipeInfo: RecipeInfo

)

data class RecipeInfo(
    val uri: String,
    val label: String,
    val images: images,
    val source: String,
    val url: String

)

data class images(
    @Json(name = "THUMBNAIL") val thumbnailInfo: ImageAttr,
    @Json(name = "SMALL") val smallInfo: ImageAttr,
    @Json(name = "REGULAR") val regularInfo: ImageAttr,
    @Json(name = "LARGE") val largeInfo: ImageAttr


)

data class ImageAttr(
    val url: String,
    val width: Int,
    val height: Int

)





