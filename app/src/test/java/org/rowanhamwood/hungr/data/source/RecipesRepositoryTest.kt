package org.rowanhamwood.hungr.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.rowanhamwood.hungr.utils.CoroutineRule
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.getOrAwaitValue
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.repository.RecipesRepository


@RunWith(RobolectricTestRunner::class)
class RecipesRepositoryTest {


    // use fake data sources to test repository
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var remoteDataSource: FakeRemoteDataSource
    private lateinit var ioDispatcher: CoroutineDispatcher
    private lateinit var recipesRepository: BaseRecipesRepository
    private val e: Exception = Exception()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = CoroutineRule()


    @ExperimentalCoroutinesApi
    @Before
    fun setupRepository() {

        localDataSource = FakeLocalDataSource()
        remoteDataSource = FakeRemoteDataSource()
        ioDispatcher = Dispatchers.Main


        localDataSource.exception = e

        val recipe1 =
            RecipeModel("recipe1", "label1", "largeImage1", "smallImage1", "source1", "url1")
        val recipe2 =
            RecipeModel("recipe2", "label2", "largeImage2", "smallImage2", "source2", "url2")
        remoteDataSource.addRecipes(recipe1, recipe2)

        //add recipes for second page of data
        val recipe3 =
            RecipeModel("recipe3", "label3", "largeImage3", "smallImage3", "source3", "url3")
        val recipe4 =
            RecipeModel("recipe4", "label4", "largeImage4", "smallImage4", "source4", "url4")
        remoteDataSource.addRecipes(recipe3, recipe4)

        //add recipes for third page of data
        val recipe5 =
            RecipeModel("recipe5", "label5", "largeImage5", "smallImage5", "source5", "url5")
        val recipe6 =
            RecipeModel("recipe6", "label6", "largeImage6", "smallImage6", "source6", "url6")
        remoteDataSource.addRecipes(recipe5, recipe6)

        //favourite recipes
        val databaseRecipe1 = DatabaseRecipe("recipe1", "label1", "image1", "source1", "url1")
        val databaseRecipe2 = DatabaseRecipe("recipe2", "label2", "image2", "source2", "url2")
        localDataSource.addFavRecipes(databaseRecipe1, databaseRecipe2)

        recipesRepository = RecipesRepository(localDataSource, remoteDataSource, ioDispatcher)


    }



    @ExperimentalCoroutinesApi
    @Test
    fun insertRecipeToDatabaseFailure() = runTest {
        //Given success is set to false
        localDataSource.insertRecipesSuccess = false


        //When a recipe is inserted
        //Then insert recipe returns false
        assertThat(
            recipesRepository.insertRecipe
                (RecipeModel("uri", "label", "largeImage", "smallimage", "source", "url")),
            `is`(false)
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertRecipeToDatabaseSuccess() = runTest {
        //Given success is set to false
        localDataSource.insertRecipesSuccess = true


        //When a recipe is inserted
        //Then insert recipe returns false
        assertThat(
            recipesRepository.insertRecipe
                (RecipeModel("uri", "label", "largeImage", "smallimage", "source", "url")),
            `is`(true)
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteRecipeFromDatabase() = runTest {


        //When a recipe is deleted
        recipesRepository.deleteRecipe(DatabaseRecipe("uri", "label", "image", "source", "url"))
        //Then the correct recipe is passed
        assertThat(
            localDataSource.deletedRecipe,
            `is`(DatabaseRecipe("uri", "label", "image", "source", "url"))
        )

    }


    @Test
    fun getFavRecipesSuccess() {

        //Given that get favourite recipe is successful
        localDataSource.favRecipesSuccess = true
        localDataSource.setFavRecipes()

        // When favRecipes is updated Then is Result.Success and correct data
        assertThat(
            recipesRepository.favouriteRecipes.getOrAwaitValue(),
            `is`(Result.Success(localDataSource.favRecipesServiceData.values.toList()))
        )

    }


    @Test
    fun getFavRecipesFailure() {

        //Given that get favourite recipe is a failure
        localDataSource.favRecipesSuccess = false
        localDataSource.setFavRecipes()

        // When favRecipes is updated Then is Result.Error with an Exception
        assertThat(recipesRepository.favouriteRecipes.getOrAwaitValue(), `is`(Result.Error(e)))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getRecipes_AllQueriesfilledPassedCorrectly_WithAppNewStartAndGetNextFalse() = runTest {

        //Given that search is a success
        remoteDataSource.searchSuccess = true

        // When getRecipes is called with all queries filled Then Result.Success is returned and data is correct
        assertThat(
            recipesRepository.getRecipes("PIE", "VEGAN", "CHINESE", false, false), `is`
                (Result.Success(remoteDataSource.recipesLiveData))
        )
        assertThat(
            remoteDataSource.recipesLiveData.value,
            `is`(remoteDataSource.recipesServiceData.values.toList())
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getRecipeData_SearchQueryFilledTwoEmptyStringPassedCorrectly_GetNextAndAppNewStartFalse() =
        runTest {
            //Given that search is a success
            remoteDataSource.searchSuccess = true

            // When getRecipes is called with all queries filled Then Result.Success is returned and data is correct
            assertThat(
                recipesRepository.getRecipes("PIE", "", "", false, false), `is`
                    (Result.Success(remoteDataSource.recipesLiveData))
            )
            assertThat(
                remoteDataSource.recipesLiveData.value,
                `is`(remoteDataSource.recipesServiceData.values.toList())
            )


        }

    @ExperimentalCoroutinesApi
    @Test
    fun getRecipeData_SearchQueryfilledTwoNullPassedCorrectly_GetNextAndAppNewStartFalse() =
        runTest {
            //Given that search is a success
            remoteDataSource.searchSuccess = true

            // When getRecipes is called with all queries filled Then Result.Success is returned and data is correct
            assertThat(
                recipesRepository.getRecipes("PIE", null, null, false, false), `is`
                    (Result.Success(remoteDataSource.recipesLiveData))
            )
            assertThat(
                remoteDataSource.recipesLiveData.value,
                `is`(remoteDataSource.recipesServiceData.values.toList())
            )

        }

    @ExperimentalCoroutinesApi
    @Test
    fun getRecipeData_AllQueriesfilledPassedCorrectlyAndGetNextAndAppNewStart() = runTest {
            //Given that search is a success
            remoteDataSource.searchSuccess = true

            // When getRecipes is called with all queries filled Then Result.Success is returned and data is correct
            assertThat(
                recipesRepository.getRecipes("PIE", "VEGAN", "CHINESE", true, true), `is`
                    (Result.Success(remoteDataSource.recipesLiveData))
            )
            assertThat(
                remoteDataSource.recipesLiveData.value,
                `is`(remoteDataSource.recipesSecondPageServiceData.values.toList())
            )

        }

    @ExperimentalCoroutinesApi
    @Test
    fun getRecipeData_SearchQueryFilledTwoEmptyStringPassedCorrectlyAndGetNextAndAppNewStart() = runTest {
        //Given that search is a success
        remoteDataSource.searchSuccess = true

        // When getRecipes is called with all queries filled Then Result.Success is returned and data is correct
        assertThat(
            recipesRepository.getRecipes("PIE", "", "", true, true), `is`
                (Result.Success(remoteDataSource.recipesLiveData))
        )
        assertThat(
            remoteDataSource.recipesLiveData.value,
            `is`(remoteDataSource.recipesSecondPageServiceData.values.toList())
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getRecipeData_SearchQueryfilledTwoNullPassedCorrectlyAndGetNextAndAppNewStart() = runTest {
        //Given that search is a success
        remoteDataSource.searchSuccess = true

        // When getRecipes is called with all queries filled Then Result.Success is returned and data is correct
        assertThat(
            recipesRepository.getRecipes("PIE", null, null, true, true), `is`
                (Result.Success(remoteDataSource.recipesLiveData))
        )
        assertThat(
            remoteDataSource.recipesLiveData.value,
            `is`(remoteDataSource.recipesSecondPageServiceData.values.toList())
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getRecipeData_AllQueriesfilledPassedCorrectlyAndGetNext() = runTest {
        //Given that search is a success
        remoteDataSource.searchSuccess = true

        // When getRecipes is called with all queries filled Then Result.Success is returned and data is correct
        assertThat(
            recipesRepository.getRecipes("PIE", "VEGAN", "CHINESE", true, false), `is`
                (Result.Success(remoteDataSource.recipesLiveData))
        )
        assertThat(
            remoteDataSource.recipesLiveData.value,
            `is`(remoteDataSource.recipesThirdPageServiceData.values.toList())
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getRecipeData_SearchQueryFilledTwoEmptyStringPassedCorrectlyAndGetNext() = runTest {
        //Given that search is a success
        remoteDataSource.searchSuccess = true

        // When getRecipes is called with all queries filled Then Result.Success is returned and data is correct
        assertThat(
            recipesRepository.getRecipes("PIE", "", "", true, false), `is`
                (Result.Success(remoteDataSource.recipesLiveData))
        )
        assertThat(
            remoteDataSource.recipesLiveData.value,
            `is`(remoteDataSource.recipesThirdPageServiceData.values.toList())
        )

    }




    @ExperimentalCoroutinesApi
    @Test
    fun getRecipeData_SearchQueryfilledTwoNullPassedCorrectlyAndGetNext() = runTest {
        //Given that search is a success
        remoteDataSource.searchSuccess = true

        // When getRecipes is called with all queries filled Then Result.Success is returned and data is correct
        assertThat(
            recipesRepository.getRecipes("PIE", null, null, true, false), `is`
                (Result.Success(remoteDataSource.recipesLiveData))
        )
        assertThat(
            remoteDataSource.recipesLiveData.value,
            `is`(remoteDataSource.recipesThirdPageServiceData.values.toList())
        )

    }




}