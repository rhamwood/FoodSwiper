package org.rowanhamwood.hungr.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.*

import kotlinx.coroutines.launch
import org.rowanhamwood.hungr.database.DatabaseRecipe
import org.rowanhamwood.hungr.database.getDatabase
import org.rowanhamwood.hungr.network.*
import org.rowanhamwood.hungr.repository.RecipesRepository


private const val TAG = "RecipeViewModel"

class RecipeViewModel(application: Application): AndroidViewModel(application) {

    private val recipesRepository = RecipesRepository(getDatabase(application))

    val favouriteRecipes = recipesRepository.recipes

    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> = _recipes

//    private val tempRecipeList = ArrayList<DatabaseRecipe>()
//    private val _favouriteRecipes = MutableLiveData<List<DatabaseRecipe>>()
//    val favouriteRecipes: LiveData<List<DatabaseRecipe>> = _favouriteRecipes

    private val _search = MutableLiveData<String>("cake")
    val search: LiveData<String> = _search

    private val _cuisine = MutableLiveData<String?>()
    val cuisine: LiveData<String?> = _cuisine

    private val _health = MutableLiveData<String?>()
    val health: LiveData<String?> = _health

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?> = _url

    private var nextUrl: String? = null





    fun setSearch(searchText: String) {
        _search.value = searchText
        Log.d(TAG, "setSearch: search is ${search.value}")
    }

    fun setCuisine(cuisineName: String?) {
        _cuisine.value = cuisineName
        Log.d(
            TAG,
            "setCuisine: cuisine is ${cuisine.value}"
        )
    }

    fun setHealth(healthText: String?) {
        _health.value = healthText
        Log.d(TAG, "setSearch: diet is ${health.value}")
    }

    fun setFavouriteRecipes(recipe: RecipeModel) {
//        recipe?.let { tempRecipeList.add(it) }
//        _favouriteRecipes.value = tempRecipeList
        val databaseRecipe = maptoDataBaseModel(recipe)
        viewModelScope.launch {
            recipesRepository.insertRecipes(databaseRecipe)
        }
    }

    fun setUrl(url: String?) {
        _url.value = url
    }

    init {
        getRecipeData()
    }


    fun getRecipeData() {
        Log.d(TAG, "getRecipeData: starts")
        viewModelScope.launch {
            try {
                Log.d(TAG, "getRecipeData: coroutine try starts")
                val requestValue = RecipeApi.retrofitService.getRecipes(
                    searchQuery = _search.value!!,
                    healthQuery = _health.value,
                    cuisineQuery = _cuisine.value
                )
                Log.d(TAG, "getRecipeData: $requestValue")
                if (requestValue.recipeList.size > 0)
                    _recipes.value = requestValue.asRecipeModel()
                nextUrl = requestValue.nextLink?.next?.href
                Log.d(TAG, "getRecipeData: ${nextUrl}")
                Log.d(TAG, "getRecipeData: Success")

            }  catch (e: Exception) {
                Log.d(TAG, "getRecipeData: $e")
                Log.d(TAG, "getRecipeData: Failed")

        }
        }

    }

    fun getNext() {
        try {
            viewModelScope.launch {
                Log.d(TAG, "getNext value: ${nextUrl}")
                val requestValue = nextUrl?.let {
                    RecipeApi.retrofitService.getNext(
                        it

                    )
                }
                _recipes.value = requestValue?.asRecipeModel()
                nextUrl = requestValue?.nextLink?.next?.href

                Log.d(TAG, "getNext: ${requestValue.toString()}")
            }
        } catch (e: Exception) {
            Log.d(TAG, "getNext: Failed")
        }


    }

    fun maptoDataBaseModel(recipeModel: RecipeModel): DatabaseRecipe {
        return recipeModel.let {
            DatabaseRecipe(
                uri = it.uri,
                label = it.label,
                image = it.image,
                source = it.source,
                url = it.url
            )
        }
    }


}

