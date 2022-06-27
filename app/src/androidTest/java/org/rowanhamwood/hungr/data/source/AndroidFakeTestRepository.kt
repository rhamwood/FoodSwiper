package org.rowanhamwood.hungr.data.source

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.getOrAwaitValueAndroidTest
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.recipeModel1
import org.rowanhamwood.hungr.recipeModel2
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import javax.inject.Inject

private const val TAG = "AndroidFakeTestReposito"


class AndroidFakeTestRepository @Inject constructor(): BaseRecipesRepository {


    private val _favouriteRecipes: MutableLiveData<Result<List<DatabaseRecipe>>> = MutableLiveData()
    override val favouriteRecipes: LiveData<Result<List<DatabaseRecipe>>> = _favouriteRecipes


    var recipesServiceData: LinkedHashMap<String, RecipeModel> = LinkedHashMap()
    var favRecipesServiceData: LinkedHashMap<String, DatabaseRecipe> = LinkedHashMap()



    private val recipesLiveData = MutableLiveData<List<RecipeModel>>()

    var favRecipesSuccess: Boolean = true
    lateinit var deletedRecipe: DatabaseRecipe


    override suspend fun getRecipes(
        searchQuery: String?,
        healthQuery: String?,
        cuisineQuery: String?,
        getNext: Boolean,
        appNewStart: Boolean
    ): Result<LiveData<List<RecipeModel>>> {
//        if (searchQuery != null) {
            addRecipes(recipeModel1, recipeModel2)
            recipesLiveData.value = recipesServiceData.values.toList()
            Log.d(TAG, "getRecipes: result success")
            return Result.Success(recipesLiveData)

//        } else {
//            return Result.Error(Exception())
//        }

    }

    @VisibleForTesting
    fun addRecipes(vararg recipeModels: RecipeModel) {
        for (recipemodel in recipeModels) {
            recipesServiceData[recipemodel.label] = recipemodel
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
            _favouriteRecipes.value = Result.Error(Exception())
        }
    }


    override suspend fun insertRecipe(favouriteRecipe: RecipeModel): Boolean {

        val favDatabaseRecipe = maptoDataBaseModel(favouriteRecipe)
        addFavRecipes(favDatabaseRecipe)
        _favouriteRecipes.value = Result.Success(favRecipesServiceData.values.toList())
        return false


    }

    override suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe) {
        deletedRecipe = favouriteRecipe

    }

    private fun maptoDataBaseModel(recipeModel: RecipeModel): DatabaseRecipe {
        return recipeModel.let {
            DatabaseRecipe(
                uri = it.uri,
                label = it.label,
                image = it.smallImage,
                source = it.source,
                url = it.url
            )
        }
    }
}