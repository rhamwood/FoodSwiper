package org.rowanhamwood.hungr.repository

import androidx.lifecycle.LiveData
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.ResultState
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.Recipe
import org.rowanhamwood.hungr.remote.network.RecipeModel

interface BaseRecipesRepository {

    val favouriteRecipes: LiveData<Result<List<DatabaseRecipe>>>

    suspend fun getRecipes(
        searchQuery: String?,
        healthQuery: String?,
        cuisineQuery: String?,
        getNext: Boolean,
        appNewStart: Boolean
    ): Result<LiveData<List<RecipeModel>>>

    suspend fun insertRecipe(favouriteRecipe: RecipeModel) : Boolean

    suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe)


}