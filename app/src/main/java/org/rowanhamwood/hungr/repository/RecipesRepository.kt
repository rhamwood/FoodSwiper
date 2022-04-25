package org.rowanhamwood.hungr.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.local.database.asDomainModel
import org.rowanhamwood.hungr.remote.BaseRemoteDataSource
import org.rowanhamwood.hungr.remote.network.RecipeModel

private const val TAG = "RecipesRepository"


class RecipesRepository(
    private val baseLocalDataSource: BaseLocalDataSource,
    private val baseRemoteDataSource: BaseRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRecipesRepository {

    override val favouriteRecipes: LiveData<List<RecipeModel>> =
            Transformations.map(baseLocalDataSource.getRecipes()) {
                it.asDomainModel()
            }





    override val recipes: LiveData<List<RecipeModel>> = baseRemoteDataSource.recipes


    override suspend fun getRecipes(searchQuery: String, healthQuery: String?, cuisineQuery: String?) {
       baseRemoteDataSource.getRecipes(searchQuery, healthQuery, cuisineQuery)
    }

    override suspend fun getNext() {
       baseRemoteDataSource.getNext()
    }


     override suspend fun insertRecipe(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            baseLocalDataSource.insertRecipe(favouriteRecipe)
        }
    }

    override suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            baseLocalDataSource.deleteRecipe(favouriteRecipe)
        }
    }
}

