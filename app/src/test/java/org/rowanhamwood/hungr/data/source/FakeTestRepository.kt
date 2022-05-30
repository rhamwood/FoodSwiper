package org.rowanhamwood.hungr.data.source

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import java.lang.NullPointerException


class FakeTestRepository : BaseRecipesRepository {


    private val _favouriteRecipes: MutableLiveData<Result<List<DatabaseRecipe>>> = MutableLiveData()
    override val favouriteRecipes: LiveData<Result<List<DatabaseRecipe>>> = _favouriteRecipes


    var recipesServiceData: LinkedHashMap<String, RecipeModel> = LinkedHashMap()
    var recipesSecondPageServiceData: LinkedHashMap<String, RecipeModel> = LinkedHashMap()
    var recipesThirdPageServiceData: LinkedHashMap<String, RecipeModel> = LinkedHashMap()
    var favRecipesServiceData: LinkedHashMap<String, DatabaseRecipe> = LinkedHashMap()


    val recipesLiveData = MutableLiveData<List<RecipeModel>>()

    var searchSuccess: Boolean = true
    var favRecipesSuccess: Boolean = true
    lateinit var deletedRecipe: DatabaseRecipe


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

    fun addFavRecipes(vararg databaseRecipes: DatabaseRecipe) {
        for (databaserecipe in databaseRecipes) {
            favRecipesServiceData[databaserecipe.label] = databaserecipe
        }

    }

    fun setFavRecipes() {
        if (favRecipesSuccess) {
            _favouriteRecipes.value = Result.Success(favRecipesServiceData.values.toList())
        } else {
            _favouriteRecipes.value = Result.Error(NetworkErrorException())
        }
    }


    override suspend fun insertRecipe(favouriteRecipe: RecipeModel): Boolean {

        if (favouriteRecipe.smallImage == "validSmallImage") {
            return true
        }
        return false


    }

    override suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe) {
        deletedRecipe = favouriteRecipe

    }
}