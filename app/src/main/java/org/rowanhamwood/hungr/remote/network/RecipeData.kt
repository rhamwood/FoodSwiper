package org.rowanhamwood.hungr.remote.network

import com.squareup.moshi.Json



data class RecipeData(
    @Json(name = "_links") val nextLink: Link?,
    @Json(name = "hits") val recipeList: List<Recipe>
    )




fun RecipeData.asRecipeModel(): List<RecipeModel> {
    return recipeList.map {
        RecipeModel(
            uri = it.recipeInfo.uri,
            label = it.recipeInfo.label,
            image = it.recipeInfo.image,
            source = it.recipeInfo.source,
            url = it.recipeInfo.url
        )
    }


}




