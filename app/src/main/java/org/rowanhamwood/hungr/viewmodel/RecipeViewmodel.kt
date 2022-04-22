package org.rowanhamwood.hungr.viewmodel

import android.util.Log
import androidx.lifecycle.*

import kotlinx.coroutines.launch
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.*
import org.rowanhamwood.hungr.repository.BaseRecipesRepository


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
    }

    fun setCuisine(cuisineName: String?) {
        _cuisine.value = cuisineName
    }

    fun setHealth(healthText: String?) {
        _health.value = healthText
    }

    fun setFavouriteRecipes(recipe: RecipeModel) {
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
        if (searchQuery!= null) {
            val healthQuery = _health.value
            val cuisineQuery = _cuisine.value
            viewModelScope.launch {
                recipesRepository.getRecipes(searchQuery, healthQuery, cuisineQuery)
            }
        } else{
            Log.d(TAG, "getRecipeData: search value is null, cannot get recipe data")
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
