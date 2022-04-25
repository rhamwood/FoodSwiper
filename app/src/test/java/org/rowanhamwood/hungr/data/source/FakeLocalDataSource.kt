package org.rowanhamwood.hungr.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel

class FakeLocalDataSource(var recipes: MutableList<DatabaseRecipe>? = mutableListOf()) : BaseLocalDataSource{


    private val _favouriteRecipes = MutableLiveData<List<DatabaseRecipe>>()
    val favouriteRecipes: LiveData<List<DatabaseRecipe>> = _favouriteRecipes


    override suspend fun insertRecipe(favouriteRecipe: DatabaseRecipe) {
        recipes?.add(favouriteRecipe)
    }

    override suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe) {
        recipes?.remove(favouriteRecipe)
    }

    override fun getRecipes(): LiveData<List<DatabaseRecipe>> {
        _favouriteRecipes.value = recipes
        return favouriteRecipes
    }
}
