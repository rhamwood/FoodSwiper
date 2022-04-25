package org.rowanhamwood.hungr.remote

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.remote.network.Next
import org.rowanhamwood.hungr.remote.network.RecipeApi
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.remote.network.asRecipeModel

private const val TAG = "RemoteDataSource"

class RemoteDataSource(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    BaseRemoteDataSource {





    private var nextUrl: String? = null


    private val _recipes = MutableLiveData<List<RecipeModel>>()
    override val recipes: LiveData<List<RecipeModel>> = _recipes

    override suspend fun getRecipes(
        searchQuery: String?,
        healthQuery: String?,
        cuisineQuery: String?,
        getNext: Boolean
    ) {
        if (!getNext) {
            if (searchQuery != null){
            withContext(ioDispatcher) {
                try {
                    val requestValue = RecipeApi.retrofitService.getRecipes(
                        searchQuery = searchQuery,
                        healthQuery = healthQuery,
                        cuisineQuery = cuisineQuery
                    )
                    if (requestValue.recipeList.isNotEmpty())
                        _recipes.postValue(requestValue.asRecipeModel())
                    nextUrl = requestValue.nextLink?.next?.href




                } catch (e: Exception) {
                    Log.d(TAG, "getRecipeData: $e")
                    Log.d(TAG, "getRecipeData: Failed")

                }
            }

            } else {
                Log.d(TAG, "search query is null")
            }
        } else {
            try {
                withContext(ioDispatcher) {
                    Log.d(TAG, "getNext value: $nextUrl")
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

    }








}