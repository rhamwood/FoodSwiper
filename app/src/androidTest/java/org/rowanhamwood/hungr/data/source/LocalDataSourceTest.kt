package org.rowanhamwood.hungr.data.source

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.rowanhamwood.hungr.*
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.LocalDataSource
import org.rowanhamwood.hungr.local.database.FavouriteRecipesDatabase
import org.rowanhamwood.hungr.local.database.RecipeDao
import org.rowanhamwood.hungr.utils.AndroidCoroutineRule
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalDataSourceTest {
    private lateinit var recipeDao: RecipeDao
    private lateinit var database : FavouriteRecipesDatabase
    private lateinit var context: Context
    private lateinit var ioDispatcher: CoroutineDispatcher
    private lateinit var localDataSource: BaseLocalDataSource
    private lateinit var fileSaverService: FakeFileSaverService



    @Before
    fun createDbAndLocalDatasource() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, FavouriteRecipesDatabase::class.java)
            .build()
        recipeDao = database.recipeDao
        ioDispatcher = Dispatchers.Main
        fileSaverService = FakeFileSaverService()



    }


    @get:Rule
    var coroutineRule = AndroidCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }




    @Test
    fun insertRecipeToDatabase()  = runTest {

        //Given
        localDataSource = LocalDataSource(recipeDao, ioDispatcher, fileSaverService)

        //When, Then
        assertThat(localDataSource.insertRecipe(recipeModel1),`is`(true))


    }

    @Test
    fun deleteRecipeFromDatabase()  = runTest {

        //Given
        localDataSource = LocalDataSource(recipeDao, ioDispatcher, fileSaverService)
        localDataSource.insertRecipe(recipeModel1)

        //When
        localDataSource.deleteRecipe(databaseRecipe1)

        //Then
        assert(!localDataSource.isRecipeSaved(databaseRecipe1.label))

    }

    @Test
    fun retrieveSavedRecipesLivedataFromDatabase() = runTest {

        //Given
        localDataSource = LocalDataSource(recipeDao, ioDispatcher, fileSaverService)
        localDataSource.insertRecipe(recipeModel1)
        localDataSource.insertRecipe(recipeModel2)

        //When, Then
        assertThat(localDataSource.getRecipes().getOrAwaitValueAndroidTest(), `is` (
            Result.Success(
                responseDatabaseRecipeList
            )
        ))


    }










}