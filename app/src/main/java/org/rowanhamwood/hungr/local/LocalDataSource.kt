package org.rowanhamwood.hungr.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.local.database.RecipeDao
import org.rowanhamwood.hungr.local.database.asDomainModel
import org.rowanhamwood.hungr.remote.network.RecipeModel

class LocalDataSource (private val recipeDao: RecipeDao,
                       private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    BaseLocalDataSource {

    override suspend fun insertRecipes(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            recipeDao.insertRecipe(favouriteRecipe)
        }
    }

    override suspend fun deleteRecipes(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            recipeDao.deleteRecipe(favouriteRecipe)
        }
    }

    override fun getRecipes() : LiveData<List<DatabaseRecipe>> {
        return recipeDao.getRecipes()
    }


}