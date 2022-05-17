package org.rowanhamwood.hungr.local

import androidx.lifecycle.LiveData
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.database.DatabaseRecipe


interface BaseLocalDataSource {
    suspend fun insertRecipe(favouriteRecipe: DatabaseRecipe)

    suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe)

    fun getRecipes(): LiveData<Result<List<DatabaseRecipe>>>
}