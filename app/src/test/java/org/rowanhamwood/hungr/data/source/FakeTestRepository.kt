package org.rowanhamwood.hungr.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.repository.BaseRecipesRepository

class FakeTestRepository : BaseRecipesRepository {


    var favouriteRecipesTestData: LinkedHashMap<String, RecipeModel> = LinkedHashMap()

    private val _favouriteRecipes = MutableLiveData<List<RecipeModel>>()
    override val favouriteRecipes: LiveData<List<RecipeModel>> = _favouriteRecipes

    var recipesTestData: LinkedHashMap<String, RecipeModel> = LinkedHashMap()

    private val _recipes = MutableLiveData<List<RecipeModel>>()
    override val recipes: LiveData<List<RecipeModel>> = _recipes

    lateinit var insertedRecipe: DatabaseRecipe
    lateinit var deletedRecipe: DatabaseRecipe

    lateinit var mSearchQuery: String
    var mHealthQuery: String? = null
    var mCuisineQuery: String? = null
    var nextValue = false


    fun addRecipes(vararg recipeModels: RecipeModel) {
        for (recipeModel in recipeModels) {
            recipesTestData[recipeModel.uri] = recipeModel
        }
        runBlocking { _recipes.value = recipesTestData.values.toList() }
    }

    fun addFavouriteRecipes(vararg recipeModels: RecipeModel) {
        for (recipeModel in recipeModels) {
            favouriteRecipesTestData[recipeModel.uri] = recipeModel
        }
        runBlocking { _recipes.value = recipesTestData.values.toList() }
    }


    override suspend fun getRecipes(
        searchQuery: String,
        healthQuery: String?,
        cuisineQuery: String?
    ) {
//        val recipeModel1 = RecipeModel("uri", "$searchQuery 1" , "image", "source", "url")
//        val recipeModel2 = RecipeModel("uri", "$searchQuery 2" , "image", "source", "url")
//        val recipeModel3 = RecipeModel("uri", "$searchQuery 2" , "image", "source", "url")
//
//        addRecipes(recipeModel1, recipeModel2, recipeModel3)
        if (searchQuery!= null) {
            mSearchQuery = searchQuery
            mHealthQuery = healthQuery
            mCuisineQuery = cuisineQuery
        }


    }

    override suspend fun getNext() {
        nextValue = true
    }

    override suspend fun insertRecipes(favouriteRecipe: DatabaseRecipe) {
        insertedRecipe = favouriteRecipe
    }

    override suspend fun deleteRecipes(favouriteRecipe: DatabaseRecipe) {
        deletedRecipe = favouriteRecipe
    }
}