package org.rowanhamwood.hungr.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.local.database.RecipeDao
import org.rowanhamwood.hungr.local.database.asDomainModel
import org.rowanhamwood.hungr.remote.network.RecipeModel

private const val TAG = "LocalDataSource"

class LocalDataSource (private val recipeDao: RecipeDao,
                       private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    BaseLocalDataSource {

    override suspend fun insertRecipe(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            recipeDao.insertRecipe(favouriteRecipe)
        }
    }

    override suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            recipeDao.deleteRecipe(favouriteRecipe)
        }
    }

    override fun getRecipes() : LiveData<Result<List<DatabaseRecipe>>> {
           return recipeDao.getRecipes().map { Result.Success(it) }

    }


}