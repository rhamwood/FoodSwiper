package org.rowanhamwood.hungr.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.BaseRemoteDataSource
import org.rowanhamwood.hungr.remote.network.RecipeModel

class FakeRemoteDataSource (var recipeList: MutableList<RecipeModel>? = mutableListOf()): BaseRemoteDataSource {



    private val _recipes = MutableLiveData<List<RecipeModel>>()
    override val recipes: LiveData<List<RecipeModel>> = _recipes

    var nextValuePassed = false
    var getNextCalled = false
    var mSearchQuery: String? = null
    var mHealthQuery: String? = null
    var mCuisineQuery: String? = null


    override suspend fun getRecipes(
        searchQuery: String,
        healthQuery: String?,
        cuisineQuery: String?
    ) {


        if (recipeList!= null) {
            _recipes.value = recipeList
            nextValuePassed = true
            mSearchQuery = searchQuery
            mHealthQuery = healthQuery
            mCuisineQuery = cuisineQuery

        } else {
            Exception("search failed")
        }



    }

    override suspend fun getNext() {
        if (nextValuePassed) {
            getNextCalled = true
        } else {
            Exception("next value not passed")
        }
    }
}