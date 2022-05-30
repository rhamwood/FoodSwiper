package org.rowanhamwood.hungr.viewmodel


import android.accounts.NetworkErrorException
import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


import org.rowanhamwood.hungr.*
import org.rowanhamwood.hungr.data.source.FakeTestRepository

@RunWith(RobolectricTestRunner::class)
class RecipeViewModelTest {

    // Use a fake repository to be injected into the viewmodel
    private lateinit var recipesRepository: FakeTestRepository
    private lateinit var sharedPreferences: SharedPreferences

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
        //add recipes for first page of data
        val recipe1 = RecipeModel("recipe1", "label1", "largeImage1", "smallImage1", "source1", "url1")
        val recipe2 = RecipeModel("recipe2", "label2", "largeImage2", "smallImage2", "source2", "url2")
        recipesRepository.addRecipes(recipe1, recipe2)

        //add recipes for second page of data
        val recipe3 = RecipeModel("recipe3", "label3", "largeImage3", "smallImage3", "source3", "url3")
        val recipe4 = RecipeModel("recipe4", "label4", "largeImage4", "smallImage4", "source4", "url4")
        recipesRepository.addSecondPageRecipes(recipe3,recipe4)

        //add recipes for third page of data
        val recipe5 = RecipeModel("recipe5", "label5", "largeImage5", "smallImage5", "source5", "url5")
        val recipe6 = RecipeModel("recipe6", "label6", "largeImage6", "smallImage6", "source6", "url6")
        recipesRepository.addThirdPageRecipes(recipe5, recipe6)

        //favourite recipes
        val databaseRecipe1 = DatabaseRecipe("recipe1", "label1",  "image1", "source1", "url1")
        val databaseRecipe2 = DatabaseRecipe("recipe2", "label2",  "image2", "source2", "url2")
        recipesRepository.addFavRecipes(databaseRecipe1, databaseRecipe2)


        sharedPreferences = getApplicationContext<Context>().getSharedPreferences("org.rowanhamwood.hungr.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        recipeViewModel = RecipeViewModel(recipesRepository, sharedPreferences)




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
    fun getFavouriteRecipesSuccess(){
        //When recipes are retrieved successfully
        recipesRepository.favRecipesSuccess = true
        recipesRepository.setFavRecipes()


        //fav recipe ui state is Success and favRecipesLivedata holds correct data
        assertThat(recipeViewModel.favouriteRecipes.getOrAwaitValue(), `is`(recipesRepository.favRecipesServiceData.values.toList()))
        assertThat(recipeViewModel.favRecipeUiState.getOrAwaitValue(), `is`(ResultState.Success))

    }

    @Test
    fun setFavouriteRecipes_insertRecipeCalledWithValidDatabaseRecipe() {

        //Given a valid recipeModel with a valid smallImage
        val recipeModel: RecipeModel = RecipeModel("uri", "label", "image", "validSmallImage", "source", "url")

        //When a new favourite recipe is set
        recipeViewModel.setFavouriteRecipes(recipeModel)

        //Then recipeImageLoadingState LiveData is set to true
        assertThat(recipeViewModel.recipeImageLoadingState.value, `is`(true) )



    }

    @Test
    fun setFavouriteRecipes_insertRecipeCalledWithInvalidDatabaseRecipe() {

        //Given a valid recipeModel with an invalid smallImage
        val recipeModel: RecipeModel = RecipeModel("uri", "label", "image", "invalidSmallImage", "source", "url")

        //When a new favourite recipe is set
        recipeViewModel.setFavouriteRecipes(recipeModel)

        //Then recipeImageLoadingState LiveData is set to false
        assertThat(recipeViewModel.recipeImageLoadingState.value, `is`(false) )


    }



    @Test
    fun deleteFavouriteRecipes_deleteRecipeCalledWithDatabaseRecipe() {

        //Given a valid recipeModel
        val databaseRecipe: DatabaseRecipe = DatabaseRecipe("uri", "label", "image", "source", "url")

        //When a new favourite recipe is set
        recipeViewModel.deleteFavouriteRecipes(databaseRecipe)

        //Then databaseRecipe is passed
        assertThat(recipesRepository.deletedRecipe, `is`(DatabaseRecipe("uri", "label", "image", "source", "url")) )

    }

    @Test
    fun getRecipeData_AllQueriesfilledPassedCorrectly() {

        // Given search, health and cuisine Livedata is not null
        recipeViewModel.setSearch("PIE")
        recipeViewModel.setHealth("VEGAN")
        recipeViewModel.setCuisine("CHINESE")

        //When getRecipeData is called with all queries filled and getNext and appNewStart are false
        recipeViewModel.getRecipeData(false, false)

        //Then result is success and recipes livedata and Ui state are updated
        assertThat(recipeViewModel.recipes.getOrAwaitValue(), `is`(recipesRepository.recipesServiceData.values.toList()))
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Success))


    }

    @Test
    fun getRecipeData_SearchQueryFilledTwoEmptyStringPassedCorrectly() {

        // Given search is filled and other liveData are empty strings
        recipeViewModel.setSearch("PIE")
        recipeViewModel.setHealth("")
        recipeViewModel.setCuisine("")

        //When getRecipeData is called with all queries filled and getNext and appNewStart are false
        recipeViewModel.getRecipeData(false, false)

        //Then result is success and recipes livedata and Ui state are updated
        assertThat(recipeViewModel.recipes.getOrAwaitValue(), `is`(recipesRepository.recipesServiceData.values.toList()))
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Success))



    }

    @Test
    fun getRecipeData_SearchQueryfilledTwoNullPassedCorrectly() {

        // Given search is filled and other liveData are empty strings
        recipeViewModel.setSearch("PIE")

        //When getRecipeData is called with all queries filled and getNext and appNewStart are false
        recipeViewModel.getRecipeData(false, false)

        //Then repository method getRecipes returns Result.Success with first page data
        assertThat(recipeViewModel.recipes.getOrAwaitValue(), `is`(recipesRepository.recipesServiceData.values.toList()))
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Success))



    }



    @Test
    fun getRecipeData_SearchQueryFilledTwoEmptyStringPassedCorrectlyAndGetNextAndAppNewStart() {

        // Given search is filled and other liveData are empty strings
        recipeViewModel.setSearch("PIE")
        recipeViewModel.setHealth("")
        recipeViewModel.setCuisine("")

        //When getRecipeData is called with all queries filled and getNext and appNewStart are true
        recipeViewModel.getRecipeData(true, true)

        //Then result is success and recipes livedata and Ui state are updated
        assertThat(recipeViewModel.recipes.getOrAwaitValue(), `is`(recipesRepository.recipesSecondPageServiceData.values.toList()))
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Success))

    }

    @Test
    fun getRecipeData_AllQueriesfilledPassedCorrectlyAndGetNextAndAppNewStart() {

        // Given search, health and cuisine Livedata is not null
        recipeViewModel.setSearch("PIE")
        recipeViewModel.setHealth("VEGAN")
        recipeViewModel.setCuisine("CHINESE")

        //When getRecipeData is called with all queries filled and getNext and appNewStart are true
        recipeViewModel.getRecipeData(true, true)

        //Then result is success and recipes livedata and Ui state are updated
        assertThat(recipeViewModel.recipes.getOrAwaitValue(), `is`(recipesRepository.recipesSecondPageServiceData.values.toList()))
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Success))


    }

    @Test
    fun getRecipeData_SearchQueryfilledTwoNullPassedCorrectlyAndGetNextAndAppNewStart() {

        // Given search is filled and other liveData are empty strings
        recipeViewModel.setSearch("PIE")

        //When getRecipeData is called with all queries filled and getNext and appNewStart are true
        recipeViewModel.getRecipeData(true, true)

        //Then repository method getRecipes returns Result.Success with first page data
        assertThat(recipeViewModel.recipes.getOrAwaitValue(), `is`(recipesRepository.recipesSecondPageServiceData.values.toList()))
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Success))

    }

    @Test
    fun getRecipeData_SearchQueryFilledTwoEmptyStringPassedCorrectlyAndGetNext() {

        // Given search is filled and other liveData are empty strings
        recipeViewModel.setSearch("PIE")
        recipeViewModel.setHealth("")
        recipeViewModel.setCuisine("")

        //When getRecipeData is called with all queries filled and getNext and appNewStart are false
        recipeViewModel.getRecipeData(true, false)

        //Then result is success and recipes livedata and Ui state are updated
        assertThat(recipeViewModel.recipes.getOrAwaitValue(), `is`(recipesRepository.recipesThirdPageServiceData.values.toList()))
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Success))

    }

    @Test
    fun getRecipeData_AllQueriesfilledPassedCorrectlyAndGetNext() {

        // Given search, health and cuisine Livedata is not null
        recipeViewModel.setSearch("PIE")
        recipeViewModel.setHealth("VEGAN")
        recipeViewModel.setCuisine("CHINESE")

        //When getRecipeData is called with all queries filled and getNext is true appNewStart is false
        recipeViewModel.getRecipeData(true, false)

        //Then result is success and recipes livedata and Ui state are updated
        assertThat(recipeViewModel.recipes.getOrAwaitValue(), `is`(recipesRepository.recipesThirdPageServiceData.values.toList()))
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Success))


    }

    @Test
    fun getRecipeData_SearchQueryfilledTwoNullPassedCorrectlyAndGetNext() {

        // Given search is filled and other liveData are empty strings
        recipeViewModel.setSearch("PIE")

        //When getRecipeData is called with all queries filled and getNext is true appNewStart is false
        recipeViewModel.getRecipeData(true, false)

        //Then repository method getRecipes returns Result.Success with first page data
        assertThat(recipeViewModel.recipes.getOrAwaitValue(), `is`(recipesRepository.recipesThirdPageServiceData.values.toList()))
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Success))

    }

    @Test
    fun getRecipeData_SearchQueryfilledTwoNullSearchFailed(){

        // Given search is filled and other liveData are empty strings, search set to fail
        recipeViewModel.setSearch("PIE")
        recipesRepository.searchSuccess = false


        //When getRecipeData is called with all queries filled and getNext is false appNewStart is false
        recipeViewModel.getRecipeData(false, false)

        //Then repository method getRecipes returns Result.Success with first page data
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Failure("Oops, something went wrong!")))

    }

    @Test
    fun getRecipeData_SearchQueryfilledTwoNullSearchFailedAndAppNewStart(){

        // Given search is filled and other liveData are empty strings, search set to fail
        recipeViewModel.setSearch("PIE")
        recipesRepository.searchSuccess = false


        //When getRecipeData is called with all queries filled and getNext is false appNewStart is true
        recipeViewModel.getRecipeData(false, true)

        //Then repository method getRecipes returns Result.Success with first page data
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Failure("Nothing here yet, try searching!")))

    }

    @Test
    fun getRecipeData_SearchQueryfilledTwoNullSearchFailedAndGetNextAndAppNewStart(){

        // Given search is filled and other liveData are empty strings, search set to fail
        recipeViewModel.setSearch("PIE")
        recipesRepository.searchSuccess = false


        //When getRecipeData is called with all queries filled and getNext is false appNewStart is true
        recipeViewModel.getRecipeData(true, true)

        //Then repository method getRecipes returns Result.Success with first page data
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Failure("Oops, something went wrong!")))

    }





    @Test
    fun getRecipeData_AllValuesNull() {

        // Given all liveData values are null (LiveData is not set)

        //When getRecipeData is called with all queries null and getNext are false
        recipeViewModel.getRecipeData(false, false)

        //Then resultState is set to failure with the correct message
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Failure("Nothing here yet, try searching for something!")))


    }

    @Test
    fun getRecipeData_AllValuesNullAndNewStart() {

        // Given all liveData values are null (LiveData is not set)

        //When getRecipeData is called with all queries null and getNext is false and newStart is true
        recipeViewModel.getRecipeData(false, true)

        //Then resultState is set to failure with the correct message
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Failure("Nothing here yet, try searching for something!")))


    }

    @Test
    fun getRecipeData_AllValuesNullAndNewStartAndGetNext() {

        // Given all liveData values are null (LiveData is not set)

        //When getRecipeData is called with all queries null and getNext is false and newStart is true
        recipeViewModel.getRecipeData(true, true)

        //Then resultState is set to failure with the correct message
        assertThat(recipeViewModel.recipesUiState.getOrAwaitValue(), `is`(ResultState.Failure("Nothing here yet, try searching for something!")))

    }



}