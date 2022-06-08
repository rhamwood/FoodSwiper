package org.rowanhamwood.hungr.data.source

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.remote.BaseRemoteDataSource
import org.rowanhamwood.hungr.remote.network.RecipeModel
import java.lang.NullPointerException

class FakeRemoteDataSource: BaseRemoteDataSource {

    var searchSuccess: Boolean = true
    val recipesLiveData = MutableLiveData<List<RecipeModel>>()

    var recipesServiceData: LinkedHashMap<String, RecipeModel> = LinkedHashMap()
    var recipesSecondPageServiceData: LinkedHashMap<String, RecipeModel> = LinkedHashMap()
    var recipesThirdPageServiceData: LinkedHashMap<String, RecipeModel> = LinkedHashMap()

    fun addRecipes(vararg recipeModels: RecipeModel) {
        for (recipemodel in recipeModels) {
            recipesServiceData[recipemodel.label] = recipemodel
        }
    }

    fun addSecondPageRecipes(vararg recipeModels: RecipeModel) {
        for (recipemodel in recipeModels) {
            recipesSecondPageServiceData[recipemodel.label] = recipemodel
        }
    }

    fun addThirdPageRecipes(vararg recipeModels: RecipeModel) {
        for (recipemodel in recipeModels) {
            recipesThirdPageServiceData[recipemodel.label] = recipemodel
        }
    }


    //return service data depending on whether the request is successful or not and whether search query is null
    // getNext and appNewStart will give different pages of results
    override suspend fun getRecipes(
        searchQuery: String?,
        healthQuery: String?,
        cuisineQuery: String?,
        getNext: Boolean,
        appNewStart: Boolean
    ): Result<LiveData<List<RecipeModel>>> {
        if (searchQuery != null && getNext && appNewStart) {

            if (searchSuccess) {
                recipesLiveData.value = recipesSecondPageServiceData.values.toList()
                return Result.Success(recipesLiveData)
            } else
            {
                return Result.Error(NetworkErrorException())
            }

        } else if (searchQuery != null && getNext) {

            if (searchSuccess) {
                recipesLiveData.value = recipesThirdPageServiceData.values.toList()
                return Result.Success(recipesLiveData)
            } else
            {
                return Result.Error(NetworkErrorException())
            }

        } else if (searchQuery != null && appNewStart) {
            if (searchSuccess) {
                recipesLiveData.value = recipesServiceData.values.toList()
                return Result.Success(recipesLiveData)
            } else
            {
                return Result.Error(NetworkErrorException())
            }

        } else if (searchQuery != null) {
            if(searchSuccess) {
                recipesLiveData.value = recipesServiceData.values.toList()
                return Result.Success(recipesLiveData)
            } else
            {
                return Result.Error(NetworkErrorException())
            }

        } else {

            return Result.Error(NullPointerException())
        }
    }
}