package org.rowanhamwood.hungr.remote


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


class RemoteDataSource(
    private val getNextDao: getNextDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    BaseRemoteDataSource {


    private var oldNextUrl: String? = null

    override suspend fun getRecipes(
        searchQuery: String?,
        healthQuery: String?,
        cuisineQuery: String?,
        getNext: Boolean,
        appNewStart: Boolean
    ): Result<LiveData<List<RecipeModel>>> = withContext(ioDispatcher) {

        val _recipes = MutableLiveData<List<RecipeModel>>()
        if (!getNext) {
            if (searchQuery != null) {

                try {
                    val requestValue = RecipeApi.retrofitService.getRecipes(
                        searchQuery = searchQuery,
                        healthQuery = healthQuery,
                        cuisineQuery = cuisineQuery
                    )
                    if (requestValue.recipeList.isNotEmpty())
                        _recipes.postValue(requestValue.asRecipeModel())
                    val nextUrl = requestValue.nextLink?.next?.href
                    getNextDao.insertGetNext(getNextUrl("NEXT", nextUrl!!))


                    return@withContext Result.Success(_recipes)

                } catch (e: Exception) {
                    return@withContext Result.Error(e)
                }


            } else {


                return@withContext Result.Error(Exception("search query is null"))
            }
        } else {

            try {
                if (appNewStart) {
                    oldNextUrl = getNextDao.getNextById("PREVIOUS").nextUrl
                } else {
                    oldNextUrl = getNextDao.getNextById("NEXT").nextUrl
                }

                val requestValue = oldNextUrl?.let {
                    RecipeApi.retrofitService.getNext(
                        it

                    )
                }
                _recipes.postValue(requestValue?.asRecipeModel())


                val nextUrl = requestValue?.nextLink?.next?.href

                getNextDao.insertGetNext(getNextUrl("PREVIOUS", oldNextUrl!!))
                getNextDao.insertGetNext(getNextUrl("NEXT", nextUrl!!))


                return@withContext Result.Success(_recipes)

            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    }

}