package org.rowanhamwood.hungr.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.LocalDataSource
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.local.database.RecipeDao
import org.rowanhamwood.hungr.local.database.asDomainModel
import org.rowanhamwood.hungr.remote.network.RecipeApi
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.remote.network.asRecipeModel

private const val TAG = "RecipesRepository"


class RecipesRepository(
    private val baseLocalDataSource: BaseLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRecipesRepository {

    override val favouriteRecipes: LiveData<List<RecipeModel>> =
        Transformations.map(baseLocalDataSource.getRecipes()) {
            it.asDomainModel()
        }

     private var nextUrl: String? = null

     private val _recipes = MutableLiveData<List<RecipeModel>>()
     override val recipes: LiveData<List<RecipeModel>> = _recipes


    override suspend fun getRecipes(searchQuery: String, healthQuery: String?, cuisineQuery: String?) {
        withContext(ioDispatcher) {
            try {
                val requestValue = RecipeApi.retrofitService.getRecipes(
                    searchQuery = searchQuery,
                    healthQuery = healthQuery,
                    cuisineQuery = cuisineQuery
                )
                if (requestValue.recipeList.size > 0)
                    _recipes.postValue(requestValue.asRecipeModel())
                nextUrl = requestValue.nextLink?.next?.href

            } catch (e: Exception) {
                Log.d(TAG, "getRecipeData: $e")
                Log.d(TAG, "getRecipeData: Failed")

            }
        }
    }

    override suspend fun getNext() {
        try {
            withContext(ioDispatcher) {
                Log.d(TAG, "getNext value: ${nextUrl}")
                val requestValue = nextUrl?.let {
                    RecipeApi.retrofitService.getNext(
                        it

                    )
                }
                _recipes.postValue(requestValue?.asRecipeModel())
                nextUrl = requestValue?.nextLink?.next?.href

                Log.d(TAG, "getNext: ${requestValue.toString()}")
            }
        } catch (e: Exception) {
            Log.d(TAG, "getNext: Failed")
        }
    }


     override suspend fun insertRecipes(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            baseLocalDataSource.insertRecipes(favouriteRecipe)
        }
    }

    override suspend fun deleteRecipes(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            baseLocalDataSource.deleteRecipes(favouriteRecipe)
        }
    }
}

