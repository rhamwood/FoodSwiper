package org.rowanhamwood.hungr.data.source

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import org.robolectric.RobolectricTestRunner
import org.rowanhamwood.hungr.MainCoroutineRule
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.repository.RecipesRepository

@RunWith(RobolectricTestRunner::class)
class RecipesRepositoryTest{


    // use fake data sources to test repository
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var remoteDataSource: FakeRemoteDataSource
    private lateinit var context: Context
    private lateinit var ioDispatcher: CoroutineDispatcher

    private lateinit var recipesRepository: BaseRecipesRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupRepository(){
        localDataSource = FakeLocalDataSource()
        remoteDataSource = FakeRemoteDataSource()
        context = ApplicationProvider.getApplicationContext()
        ioDispatcher = Dispatchers.Main

        val recipe1 = RecipeModel("recipe1", "label1", "largeImage1", "smallImage1", "source1", "url1")
        val recipe2 = RecipeModel("recipe2", "label2", "largeImage2", "smallImage2", "source2", "url2")
        remoteDataSource.addRecipes(recipe1,recipe2)

        //add recipes for second page of data
        val recipe3 = RecipeModel("recipe3", "label3", "largeImage3", "smallImage3", "source3", "url3")
        val recipe4 = RecipeModel("recipe4", "label4", "largeImage4", "smallImage4", "source4", "url4")
        remoteDataSource.addRecipes(recipe3, recipe4)

        //add recipes for third page of data
        val recipe5 = RecipeModel("recipe5", "label5", "largeImage5", "smallImage5", "source5", "url5")
        val recipe6 = RecipeModel("recipe6", "label6", "largeImage6", "smallImage6", "source6", "url6")
        remoteDataSource.addRecipes(recipe5,recipe6)

        //favourite recipes
        val databaseRecipe1 = DatabaseRecipe("recipe1", "label1",  "image1", "source1", "url1")
        val databaseRecipe2 = DatabaseRecipe("recipe2", "label2",  "image2", "source2", "url2")
        localDataSource.addFavRecipes(databaseRecipe1, databaseRecipe2)

        recipesRepository = RecipesRepository(localDataSource, remoteDataSource, context, ioDispatcher)


    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertRecipeToDatabase() = runTest{
        //Given success is set to true
        localDataSource.favRecipesSuccess = false

        //When a recipe is inserted


        //Then insert recipe returns true
        assertThat(recipesRepository.insertRecipe
            (RecipeModel("uri", "label", "largeImage", "smallimage", "source", "url")),
            `is`(false ))


    }






}