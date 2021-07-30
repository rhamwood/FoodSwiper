package org.rowanhamwood.hungr.viewmodel

import android.text.Editable
import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.rowanhamwood.hungr.network.Recipe
import org.rowanhamwood.hungr.network.RecipeApi

private const val TAG = "RecipeViewModel"

class RecipeViewModel: ViewModel() {

//        private val _recipe = MutableLiveData<Recipe>()
//        val recipe: LiveData<Recipe> = _recipe
    
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _search = MutableLiveData<String>("cake")
    val search: LiveData<String> = _search

    private val _cuisine = MutableLiveData<String>()
    val cuisine: LiveData<String> = _cuisine

    private val _diet = MutableLiveData<String>()
    val diet: LiveData<String> = _diet

    fun setSearch(searchText: String){
        _search.value = searchText
        Log.d(TAG, "setSearch: search is ${search.value}")
    }

    fun setCuisine(cuisineName: String){
        _cuisine.value = cuisineName
        Log.d(TAG, "setCuisine: cuisine is ${cuisine.value}")
    }

    fun setDiet(dietText: String){
        _diet.value = dietText
        Log.d(TAG, "setSearch: diet is ${diet.value}")
    }





    init {
        getRecipeData()
    }


    fun getRecipeData() {
        try { viewModelScope.launch {
            val requestValue = RecipeApi.retrofitService.getRecipes(search.value!!, 10, cuisine.value, diet.value)
            if (requestValue.recipeList.size > 0)
            _recipes.value = requestValue.recipeList
            Log.d(TAG, "getRecipeData: success")
        }
        } catch (e: Exception){
            Log.d(TAG, "getRecipeData: failed")
        }

    }



}