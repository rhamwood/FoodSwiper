package org.rowanhamwood.hungr.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*

import kotlinx.coroutines.launch
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.local.database.asDomainModel
import org.rowanhamwood.hungr.remote.network.*
import org.rowanhamwood.hungr.repository.BaseRecipesRepository


private const val TAG = "RecipeViewModel"
private const val CURRENT_SEARCH = "CURRENT_SEARCH"
private const val GET_NEXT = "GET_NEXT"

class RecipeViewModel(private val recipesRepository: BaseRecipesRepository, sharedPreferences: SharedPreferences):  ViewModel() {



    private val favouriteRecipesDatabaseRecipe =
        recipesRepository.favouriteRecipes.switchMap { recipesResults(it) }

    val favouriteRecipes = Transformations.map(favouriteRecipesDatabaseRecipe){
        it.asDomainModel()
    }


    fun recipesResults(recipesResult: Result<List<DatabaseRecipe>>) : LiveData<List<DatabaseRecipe>>{

        val result = MutableLiveData<List<DatabaseRecipe>>()

        if (recipesResult is Result.Success)
            viewModelScope.launch { result.value = recipesResult.data!! }


        else {
            result.value = emptyList()
            Log.d(TAG,"error loading tasks")
        }
        return result

    }

    private val _recipes: MutableLiveData<List<RecipeModel>> = MutableLiveData(emptyList())
    val recipes: LiveData<List<RecipeModel>> = _recipes

    private val _search = MutableLiveData("Pie")
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
            recipesRepository.insertRecipe(databaseRecipe)
        }
    }

    fun deleteFavouriteRecipes(recipe: RecipeModel){
        val databaseRecipe = maptoDataBaseModel(recipe)
        viewModelScope.launch {
            recipesRepository.deleteRecipe(databaseRecipe)
        }

    }

    fun setUrl(url: String?) {
        _url.value = url
    }

    init {
        val currentSearch = sharedPreferences.getString(CURRENT_SEARCH, "cake")
        //TODO setup empty welcome screen to be displayed on first opening the app replacing default cake
        if(currentSearch != null) {
            setSearch(currentSearch)
        }
        val getNext = sharedPreferences.getBoolean(GET_NEXT, false)
        Log.d(TAG, "getnext: $getNext ")
        val appNewStart = true
        getRecipeData(getNext, appNewStart)
    }

    fun getRecipeData(getNext: Boolean, appNewStart: Boolean)  {


        val searchQuery = _search.value
        if (searchQuery!= null && !getNext) {
            val healthQuery = _health.value
            val cuisineQuery = _cuisine.value
            viewModelScope.launch {
            val result =   recipesRepository.getRecipes(searchQuery, healthQuery, cuisineQuery, getNext, appNewStart)
                Log.d(TAG, "getRecipeData: $result")
                if (result is Result.Success) {
                    _recipes.value = result.data.value
                } else {
                    Log.d(TAG, "getRecipeData: could not get recipe data")
                }


            }
        } else if (searchQuery !=null && getNext){
            viewModelScope.launch {
                val result = recipesRepository.getRecipes("", "", "", getNext, appNewStart)
                Log.d(TAG, "getRecipeData: $result")
                if (result is Result.Success) {
                    _recipes.value = result.data.value
                } else {
                    Log.d(TAG, "getRecipeData: could not get recipe data")
                }


            }
            }
         else {
             Log.d(TAG, "getRecipeData: search value is null, cannot get recipe data")



        }

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




@Suppress("UNCHECKED_CAST")
class RecipeViewModelFactory (
    private val baseRecipesRepository: BaseRecipesRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (RecipeViewModel(baseRecipesRepository, sharedPreferences) as T)
}
