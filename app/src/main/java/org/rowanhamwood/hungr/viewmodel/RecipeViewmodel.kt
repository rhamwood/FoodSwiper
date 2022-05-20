package org.rowanhamwood.hungr.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.provider.Settings.Secure.getString
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.rowanhamwood.hungr.R
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.ResultState
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.*
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "RecipeViewModel"
private const val CURRENT_SEARCH = "CURRENT_SEARCH"
private const val GET_NEXT = "GET_NEXT"

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipesRepository: BaseRecipesRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {



    private val _favouriteRecipes =
        recipesRepository.favouriteRecipes.switchMap { recipesResults(it) }
    val favouriteRecipes = _favouriteRecipes

    private val _favRecipeUiState = MutableLiveData<ResultState>()
    val favRecipeUiState: LiveData<ResultState> = _favRecipeUiState

    fun setFavRecipesResultStateLoading() {
        _favRecipeUiState.value = ResultState.Loading
    }

    fun setFavRecipesResultStateSuccess() {
        _favRecipeUiState.value = ResultState.Success
    }

    private val _recipesUiState = MutableLiveData<ResultState>()
    val recipesUiState: LiveData<ResultState> = _recipesUiState

    fun setRecipesResultStateLoading() {
        _recipesUiState.value = ResultState.Loading
    }

    private val _recipeImageLoadingState = MutableLiveData<Boolean>()
    val recipeImageLoadingState: LiveData<Boolean> = _recipeImageLoadingState

    fun setRecipeImageLoadingState(state: Boolean){
        _recipeImageLoadingState.value = state
    }



    @SuppressLint("NullSafeMutableLiveData")
    fun recipesResults(recipesResult: Result<List<DatabaseRecipe>>): LiveData<List<DatabaseRecipe>> {

        val result = MutableLiveData<List<DatabaseRecipe>>()

        _favRecipeUiState.value = ResultState.Loading

        if (recipesResult is Result.Success)

            if (recipesResult.data.isNotEmpty()) {
                viewModelScope.launch {
                    result.value = recipesResult.data
                    _favRecipeUiState.value = ResultState.Success
                }
            } else {
                _favRecipeUiState.value = ResultState.Failure("Oops, nothing here yet!")
            }
        else {
            result.value = emptyList()
            _favRecipeUiState.value = ResultState.Failure("Oops, something went wrong!")
        }
        return result

    }


    private val _recipes: MutableLiveData<List<RecipeModel>> = MutableLiveData(emptyList())
    val recipes: LiveData<List<RecipeModel>> = _recipes

    private val _search = MutableLiveData<String>()
    val search: LiveData<String> = _search

    private val _cuisine = MutableLiveData<String?>()
    val cuisine: LiveData<String?> = _cuisine

    private val _health = MutableLiveData<String?>()
    val health: LiveData<String?> = _health

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?> = _url

    fun clearRecipes() {
        _recipes.value = emptyList()
    }

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

        viewModelScope.launch {
            _recipeImageLoadingState.value = recipesRepository.insertRecipe(recipe)
        }
    }

    fun deleteFavouriteRecipes(recipe: DatabaseRecipe) {
        viewModelScope.launch {
            recipesRepository.deleteRecipe(recipe)

        }
    }

    fun setUrl(url: String?) {
        _url.value = url
    }

    init {
        val currentSearch = sharedPreferences.getString(CURRENT_SEARCH, null)
        if (currentSearch != null) {
            setSearch(currentSearch)
        }
        val getNext = sharedPreferences.getBoolean(GET_NEXT, false)
        val appNewStart = true
        getRecipeData(getNext, appNewStart)
    }


    fun getRecipeData(getNext: Boolean, appNewStart: Boolean) {

        val searchQuery = _search.value
        if (searchQuery != null && !getNext) {
            val healthQuery = _health.value
            val cuisineQuery = _cuisine.value
            viewModelScope.launch {
                val result = recipesRepository.getRecipes(
                    searchQuery,
                    healthQuery,
                    cuisineQuery,
                    getNext,
                    appNewStart
                )

                if (result is Result.Success) {


                    _recipes.value = result.data.value
                    Log.d(TAG, "${recipes.value}")
                    _recipesUiState.value = ResultState.Success


                } else {
                    if (appNewStart) {
                        _recipesUiState.value =
                            ResultState.Failure("Nothing here yet, try searching!")
                    } else {
                        _recipesUiState.value = ResultState.Failure("Oops, something went wrong!")

                    }
                }


            }
        } else if (searchQuery != null && getNext) {
            viewModelScope.launch {
                val result = recipesRepository.getRecipes("", "", "", getNext, appNewStart)
                if (result is Result.Success) {
                    _recipes.value = result.data.value
                    Log.d(TAG, "${recipes.value}")
                    _recipesUiState.value = ResultState.Success
                } else {
                    _recipesUiState.value = ResultState.Failure("Oops, something went wrong!")
                    Log.d(TAG, "${recipes.value}")
                }


            }
        } else {
            _recipesUiState.value =
                ResultState.Failure("Nothing here yet, try searching for something!")
        }

    }


}


