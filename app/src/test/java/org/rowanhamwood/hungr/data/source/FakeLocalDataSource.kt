package org.rowanhamwood.hungr.data.source

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel

class FakeLocalDataSource: BaseLocalDataSource {

    lateinit var deletedRecipe: DatabaseRecipe
    lateinit var insertedRecipe: RecipeModel
    var favRecipesServiceData: LinkedHashMap<String, DatabaseRecipe> = LinkedHashMap()
    var favRecipesSuccess: Boolean = true
    val _favouriteRecipes: MutableLiveData<Result<List<DatabaseRecipe>>> = MutableLiveData()

    // set inserted recipe to check if input if correctly received
    override suspend fun insertRecipe(favouriteRecipe: RecipeModel): Boolean {
        insertedRecipe = favouriteRecipe
        return true
    }


    // set deleted recipe to check if input if correctly received
    override suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe) {
        deletedRecipe = favouriteRecipe
    }

    //return livedata with result and recipe list
    override fun getRecipes(): LiveData<Result<List<DatabaseRecipe>>> {
        return _favouriteRecipes
    }


    //add favouriterecipes to service data
    fun addFavRecipes(vararg databaseRecipes: DatabaseRecipe) {
        for (databaserecipe in databaseRecipes) {
            favRecipesServiceData[databaserecipe.label] = databaserecipe
        }

    }

    //put service data into result wrapper if request is successful, else return error result
    fun setFavRecipes() {
        if (favRecipesSuccess) {
            _favouriteRecipes.value = Result.Success(favRecipesServiceData.values.toList())
        } else {
            _favouriteRecipes.value = Result.Error(NetworkErrorException())
        }
    }

}