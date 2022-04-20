package org.rowanhamwood.hungr.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.database.DatabaseRecipe
import org.rowanhamwood.hungr.database.FavouriteRecipesDatabase
import org.rowanhamwood.hungr.database.asDomainModel
import org.rowanhamwood.hungr.network.RecipeApi
import org.rowanhamwood.hungr.network.RecipeModel
import org.rowanhamwood.hungr.network.asRecipeModel

private const val TAG = "RecipesRepository"


class RecipesRepository(
    private val database: FavouriteRecipesDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val favouriteRecipes: LiveData<List<RecipeModel>> =
        Transformations.map(database.recipeDao.getRecipes()) {
            it.asDomainModel()
        }

    private var nextUrl: String? = null

    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> = _recipes


    suspend fun getRecipes(searchQuery: String, healthQuery: String?, cuisineQuery: String?) {
        withContext(ioDispatcher) {
            try {
                Log.d(TAG, "getRecipeData: coroutine try starts")
                val requestValue = RecipeApi.retrofitService.getRecipes(
                    searchQuery = searchQuery,
                    healthQuery = healthQuery,
                    cuisineQuery = cuisineQuery
                )
                Log.d(TAG, "getRecipeData: $requestValue")
                if (requestValue.recipeList.size > 0)
                    _recipes.postValue(requestValue.asRecipeModel())
                nextUrl = requestValue.nextLink?.next?.href
                Log.d(TAG, "getRecipeData: ${nextUrl}")
                Log.d(TAG, "getRecipeData: Success")

            } catch (e: Exception) {
                Log.d(TAG, "getRecipeData: $e")
                Log.d(TAG, "getRecipeData: Failed")

            }
        }
    }

    suspend fun getNext() {
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


    suspend fun insertRecipes(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            database.recipeDao.insertRecipe(favouriteRecipe)
        }
    }

    suspend fun deleteRecipes(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            database.recipeDao.deleteRecipe(favouriteRecipe)
        }
    }
}

