package org.rowanhamwood.hungr.local

import androidx.lifecycle.LiveData
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel


interface BaseLocalDataSource {
    suspend fun insertRecipes(favouriteRecipe: DatabaseRecipe)

    suspend fun deleteRecipes(favouriteRecipe: DatabaseRecipe)

    fun getRecipes(): LiveData<List<DatabaseRecipe>>
}