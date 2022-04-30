package org.rowanhamwood.hungr.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.local.database.getNextUrl
import org.rowanhamwood.hungr.local.database.getNextDao
import org.rowanhamwood.hungr.remote.network.RecipeApi
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.remote.network.asRecipeModel

private const val TAG = "RemoteDataSource"

class RemoteDataSource(private val getNextDao: getNextDao, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    BaseRemoteDataSource {




    private val _recipes = MutableLiveData<List<RecipeModel>>()
    override val recipes: LiveData<List<RecipeModel>> = _recipes

    private var oldNextUrl :String? = null

    override suspend fun getRecipes(
        searchQuery: String?,
        healthQuery: String?,
        cuisineQuery: String?,
        getNext: Boolean,
        appNewStart: Boolean
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
                        Log.d(TAG, "getRecipes: ${requestValue.recipeList}")
                        _recipes.postValue(requestValue.asRecipeModel())
                        Log.d(TAG, "getRecipes: _recipes.postvalue called")
                    val nextUrl = requestValue.nextLink?.next?.href

                        getNextDao.insertGetNext(getNextUrl("NEXT", nextUrl!!))
                    Log.d(TAG, "getRecipes: $nextUrl")


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

                withContext(ioDispatcher) { if (appNewStart) {
                    oldNextUrl = getNextDao.getNextById("PREVIOUS").nextUrl
                } else {
                    oldNextUrl= getNextDao.getNextById("NEXT").nextUrl
                }
                    Log.d(TAG, "getNext value: $oldNextUrl")
                    val requestValue = oldNextUrl?.let {
                        RecipeApi.retrofitService.getNext(
                            it

                        )
                    }
                    _recipes.postValue(requestValue?.asRecipeModel())


                    val nextUrl = requestValue?.nextLink?.next?.href

                    getNextDao.insertGetNext(getNextUrl("PREVIOUS", oldNextUrl!!))
                    getNextDao.insertGetNext(getNextUrl("NEXT", nextUrl!!))





                    Log.d(TAG, "getNext: ${requestValue.toString()}")
                }
            } catch (e: Exception) {
                Log.d(TAG, "getNext: Failed with exception $e")
            }

        }

    }








}