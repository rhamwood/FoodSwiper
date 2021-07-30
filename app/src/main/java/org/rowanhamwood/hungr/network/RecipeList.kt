package org.rowanhamwood.hungr.network

import com.squareup.moshi.Json


class RecipeList (
    @Json(name= "results")
    val recipeList: List<Recipe>
)