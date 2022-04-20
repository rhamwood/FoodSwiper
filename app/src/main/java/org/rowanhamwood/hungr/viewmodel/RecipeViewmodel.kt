package org.rowanhamwood.hungr.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.databinding.BaseObservable
import androidx.lifecycle.*

import kotlinx.coroutines.launch
import org.rowanhamwood.hungr.database.DatabaseRecipe
import org.rowanhamwood.hungr.network.*
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.repository.RecipesRepository


private const val TAG = "RecipeViewModel"

class RecipeViewModel(private val recipesRepository: BaseRecipesRepository):  ViewModel() {



    val favouriteRecipes = recipesRepository.favouriteRecipes

//    private val _recipes = MutableLiveData<List<RecipeModel>>()
//    val recipes: LiveData<List<RecipeModel>> = _recipes

    val recipes = recipesRepository.recipes

//    private val tempRecipeList = ArrayList<DatabaseRecipe>()
//    private val _favouriteRecipes = MutableLiveData<List<DatabaseRecipe>>()
//    val favouriteRecipes: LiveData<List<DatabaseRecipe>> = _favouriteRecipes

    private val _search = MutableLiveData("cake")
    val search: LiveData<String> = _search

    private val _cuisine = MutableLiveData<String?>()
    val cuisine: LiveData<String?> = _cuisine

    private val _health = MutableLiveData<String?>()
    val health: LiveData<String?> = _health

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?> = _url





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

    fun deleteFavouriteRecipes(recipe: RecipeModel){
        val databaseRecipe = maptoDataBaseModel(recipe)
        viewModelScope.launch {
            recipesRepository.deleteRecipes(databaseRecipe)
        }

    }

    fun setUrl(url: String?) {
        _url.value = url
    }

    init {
        getRecipeData()
    }

    fun getRecipeData() {
        val searchQuery = _search.value
        val healthQuery = _health.value
        val cuisineQuery = _cuisine.value
        viewModelScope.launch {
            recipesRepository.getRecipes(searchQuery!!, healthQuery, cuisineQuery)
        }
    }


    fun getNext() {
        viewModelScope.launch {
            recipesRepository.getNext()
        }



    }

    private fun maptoDataBaseModel(recipeModel: RecipeModel): DatabaseRecipe {
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

@Suppress("UNCHECKED_CAST")
class RecipeViewModelFactory (
    private val baseRecipesRepository: BaseRecipesRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (RecipeViewModel(baseRecipesRepository) as T)
}
