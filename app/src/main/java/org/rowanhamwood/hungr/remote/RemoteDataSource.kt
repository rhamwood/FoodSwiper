package org.rowanhamwood.hungr.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.database.getNextUrl
import org.rowanhamwood.hungr.local.database.getNextDao
import org.rowanhamwood.hungr.remote.network.RecipeApi
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.remote.network.asRecipeModel

private const val TAG = "RemoteDataSource"

class RemoteDataSource(private val getNextDao: getNextDao, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    BaseRemoteDataSource {





//    override val recipes: LiveData<List<RecipeModel>> = _recipes

    private var oldNextUrl :String? = null

    override suspend fun getRecipes(
        searchQuery: String?,
        healthQuery: String?,
        cuisineQuery: String?,
        getNext: Boolean,
        appNewStart: Boolean
    ) : Result<LiveData<List<RecipeModel>>> {

        val _recipes = MutableLiveData<List<RecipeModel>>()
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

                    return@withContext Result.Success(_recipes)

                } catch (e: Exception) {
                    Log.d(TAG, "getRecipeData: $e")
                    Log.d(TAG, "getRecipeData: Failed")
                    return@withContext Result.Error(e)
                }
            }


            } else {
                Log.d(TAG, "search query is null")

                return Result.Error(Exception("search query is null"))
            }
        } else {
            withContext(ioDispatcher) {
                    try {
                        if (appNewStart) {
                            oldNextUrl = getNextDao.getNextById("PREVIOUS").nextUrl
                        } else {
                            oldNextUrl = getNextDao.getNextById("NEXT").nextUrl
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

                        return@withContext Result.Success(_recipes)

                } catch (e: Exception) {
                    Log.d(TAG, "getNext: Failed with exception $e")
                    return@withContext Result.Error(e)
                }
            }


        }
        return Result.Error(Exception("get recipes failed"))

    }








}