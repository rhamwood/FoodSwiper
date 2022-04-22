package org.rowanhamwood.hungr.remote.network

import com.squareup.moshi.Json



data class Recipe(
    @Json(name = "recipe") val recipeInfo: RecipeInfo

)

    data class RecipeInfo(
    val uri: String,
    val label: String,
    val image: String,
    val source: String,
    val url: String

)





