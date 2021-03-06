package org.rowanhamwood.hungr.local

import androidx.lifecycle.LiveData
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel


interface BaseLocalDataSource {
    suspend fun insertRecipe(favouriteRecipe: RecipeModel): Boolean

    suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe)

    fun getRecipes(): LiveData<Result<List<DatabaseRecipe>>>

    suspend fun isRecipeSaved(key: String): Boolean
}