package org.rowanhamwood.hungr.repository

import androidx.lifecycle.LiveData
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel

interface BaseRecipesRepository {

    val favouriteRecipes: LiveData<List<RecipeModel>>

    val recipes: LiveData<List<RecipeModel>>

    suspend fun getRecipes(searchQuery: String, healthQuery: String?, cuisineQuery: String?)

    suspend fun getNext()

    suspend fun insertRecipes(favouriteRecipe: DatabaseRecipe)

    suspend fun deleteRecipes(favouriteRecipe: DatabaseRecipe)


}