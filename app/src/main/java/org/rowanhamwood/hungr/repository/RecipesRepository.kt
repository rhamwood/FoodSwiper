package org.rowanhamwood.hungr.repository


import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.BaseRemoteDataSource
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.utils.wrapEspressoIdlingResource


class RecipesRepository(
    private val baseLocalDataSource: BaseLocalDataSource,
    private val baseRemoteDataSource: BaseRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRecipesRepository {

    override suspend fun getRecipes(
        searchQuery: String?,
        healthQuery: String?,
        cuisineQuery: String?,
        getNext: Boolean,
        appNewStart: Boolean
    ): Result<LiveData<List<RecipeModel>>> {
        wrapEspressoIdlingResource {
            return baseRemoteDataSource.getRecipes(
                searchQuery,
                healthQuery,
                cuisineQuery,
                getNext,
                appNewStart
            )
        }
    }

    override val favouriteRecipes: LiveData<Result<List<DatabaseRecipe>>> =
        wrapEspressoIdlingResource {
            baseLocalDataSource.getRecipes()
        }

    override suspend fun insertRecipe(favouriteRecipe: RecipeModel): Boolean =
        withContext(ioDispatcher) {
            baseLocalDataSource.insertRecipe(favouriteRecipe)
        }

    override suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            baseLocalDataSource.deleteRecipe(favouriteRecipe)
        }
    }


}



