package org.rowanhamwood.hungr.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.ResultState
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.*
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.utils.wrapEspressoIdlingResource
import javax.inject.Inject


private const val CURRENT_SEARCH = "CURRENT_SEARCH"
private const val GET_NEXT = "GET_NEXT"
private const val TAG = "RecipeViewmodel"

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipesRepository: BaseRecipesRepository,
    private val state: SavedStateHandle
) : ViewModel() {


    val getNext: LiveData<Boolean> = state.getLiveData(GET_NEXT)

    fun setGetNext(getNext: Boolean) {
        state.set(GET_NEXT, getNext)
    }

    val search: LiveData<String> = state.getLiveData(CURRENT_SEARCH)

    fun setSearch(searchText: String) {
        state.set(CURRENT_SEARCH, searchText)
    }

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

    fun setRecipeImageLoadingState(state: Boolean) {
        _recipeImageLoadingState.value = state
    }

    private val _recipes: MutableLiveData<List<RecipeModel>> = MutableLiveData(emptyList())
    val recipes: LiveData<List<RecipeModel>> = _recipes


    private val _cuisine = MutableLiveData<String?>()
    val cuisine: LiveData<String?> = _cuisine

    private val _health = MutableLiveData<String?>()
    val health: LiveData<String?> = _health

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?> = _url

    fun clearRecipes() {
        _recipes.value = emptyList()
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

        val appNewStart = true
        if (getNext.value != null) {
            getRecipeData(getNext.value!!, appNewStart)
        } else {
            getRecipeData(false, appNewStart)
        }
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


    fun getRecipeData(getNext: Boolean, appNewStart: Boolean) {
        wrapEspressoIdlingResource {
            val searchQuery = search.value
            if (searchQuery != null && !getNext) {
                val healthQuery = health.value
                val cuisineQuery = cuisine.value
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
                        _recipesUiState.value = ResultState.Success
                        Log.d(TAG, "getRecipeData: ${_recipes.value}")

                    } else {
                        if (appNewStart) {
                            _recipesUiState.value =
                                ResultState.Failure("Nothing here yet, try searching!")
                        } else {
                            _recipesUiState.value =
                                ResultState.Failure("Oops, something went wrong!")

                        }
                    }


                }
            } else if (searchQuery != null && getNext) {
                viewModelScope.launch {
                    val result = recipesRepository.getRecipes("", "", "", getNext, appNewStart)
                    if (result is Result.Success) {
                        _recipes.value = result.data.value
                        _recipesUiState.value = ResultState.Success
                    } else {
                        _recipesUiState.value = ResultState.Failure("Oops, something went wrong!")
                    }


                }
            } else {
                _recipesUiState.value =
                    ResultState.Failure("Nothing here yet, try searching for something!")
            }

        }
    }


}


