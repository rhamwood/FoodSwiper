package org.rowanhamwood.hungr.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.rowanhamwood.hungr.MainCoroutineRule
import org.rowanhamwood.hungr.data.source.FakeTestRepository
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.getOrAwaitValue
import org.rowanhamwood.hungr.remote.network.RecipeModel

class RecipeViewModelTest {

    // Use a fake repository to be injected into the viewmodel
    private lateinit var recipesRepository: FakeTestRepository

    // Subject under test
    private lateinit var recipeViewModel: RecipeViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Before
    fun setupViewModel(){
        recipesRepository = FakeTestRepository()
        recipeViewModel = RecipeViewModel(recipesRepository)


    }

    @Test
    fun setSearch_LiveDataChanged() {

            //When a new search term is set
            recipeViewModel.setSearch("PIE")

            //search LiveData is updated
            val value = recipeViewModel.search.getOrAwaitValue()
            assertThat(value, `is`("PIE"))

    }

    @Test
    fun setCuisine_LiveDataChanged() {

        //When a new cuisine type is set
        recipeViewModel.setCuisine("CHINESE")

        //cuisine LiveData is updated
        val value = recipeViewModel.cuisine.getOrAwaitValue()
        assertThat(value, `is`("CHINESE"))

    }

    @Test
    fun setHealth_LiveDataChanged() {

        //When a new health type is set
        recipeViewModel.setHealth("VEGAN")

        //health LiveData is updated
        val value = recipeViewModel.health.getOrAwaitValue()
        assertThat(value, `is`("VEGAN"))

    }

    @Test
    fun setUrl_LiveDataChanged() {

        //When a new url is set
        recipeViewModel.setUrl("http//:hello")

        //url LiveData is updated
        val value = recipeViewModel.url.getOrAwaitValue()
        assertThat(value, `is`("http//:hello"))

    }

    @Test
    fun setFavouriteRecipes_insertRecipeCalledWithDatabaseRecipe() {

        //Given a valid recipeModel
        val recipeModel: RecipeModel = RecipeModel("uri", "label", "image", "source", "url")

        //When a new favourite recipe is set
        recipeViewModel.setFavouriteRecipes(recipeModel)

        //Then recipemodel is changed to databaseRecipe
        assertThat(recipesRepository.insertedRecipe, `is`(DatabaseRecipe("uri", "label", "image", "source", "url")) )

    }

    @Test
    fun deleteFavouriteRecipes_deleteRecipeCalledWithDatabaseRecipe() {

        //Given a valid recipeModel
        val recipeModel: RecipeModel = RecipeModel("uri", "label", "image", "source", "url")

        //When a new favourite recipe is set
        recipeViewModel.deleteFavouriteRecipes(recipeModel)

        //Then recipemodel is changed to databaseRecipe
        assertThat(recipesRepository.deletedRecipe, `is`(DatabaseRecipe("uri", "label", "image", "source", "url")) )

    }

    @Test
    fun getRecipeData_AllQueriesfilledPassedCorrectly() {

        // Given search, health and cuisine Livedata is not null
        recipeViewModel.setSearch("PIE")
        recipeViewModel.setHealth("VEGAN")
        recipeViewModel.setCuisine("CHINESE")

        //When getRecipeData is called with all queries filled
        recipeViewModel.getRecipeData()

        //Then repository method getRecipes is called with correct parameters
        assertThat(recipesRepository.mSearchQuery, `is`("PIE"))
        assertThat(recipesRepository.mHealthQuery, `is`("VEGAN"))
        assertThat(recipesRepository.mCuisineQuery, `is`("CHINESE"))

    }

    @Test
    fun getRecipeData_SearchQueryFilledTwoEmptyStringPassedCorrectly() {

        // Given search is filled and other liveData are empty strings
        recipeViewModel.setSearch("PIE")
        recipeViewModel.setHealth("")
        recipeViewModel.setCuisine("")

        //When getRecipeData is called with all queries filled
        recipeViewModel.getRecipeData()

        //Then repository method getRecipes is called with correct parameters
        assertThat(recipesRepository.mSearchQuery, `is`("PIE"))
        assertThat(recipesRepository.mHealthQuery, `is`(""))
        assertThat(recipesRepository.mCuisineQuery, `is`(""))

    }

    @Test
    fun getRecipeData_SearchQueryfilledTwoNullPassedCorrectly() {

        // Given search is filled and other liveData are empty strings
        recipeViewModel.setSearch("PIE")

        //When getRecipeData is called with all queries filled
        recipeViewModel.getRecipeData()

        //Then repository method getRecipes is called with correct parameters
        assertThat(recipesRepository.mSearchQuery, `is`("PIE"))
        assertThat(recipesRepository.mHealthQuery, `is`(nullValue()))
        assertThat(recipesRepository.mCuisineQuery, `is`(nullValue()))

    }

    @Test
    fun getRecipeData_AllValuesNull() {

        // Given all liveData values are null (LiveData is not set)


        //When getRecipeData is called with all queries filled
        recipeViewModel.getRecipeData()

        //Then repository method is calle with search value "cake"
        assertThat(recipesRepository.mSearchQuery, `is`("cake"))
        assertThat(recipesRepository.mHealthQuery, `is`(nullValue()))
        assertThat(recipesRepository.mCuisineQuery, `is`(nullValue()))

    }

    @Test
    fun getNext_repositoryFunctionCalled() {

        //When getNext is called
        recipeViewModel.getNext()

        // getNext is set to true
        assertThat(recipesRepository.nextValue, `is`(true))



    }









}