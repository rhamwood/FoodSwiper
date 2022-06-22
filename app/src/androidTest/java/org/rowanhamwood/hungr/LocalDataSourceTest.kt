package org.rowanhamwood.hungr

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.LocalDataSource
import org.rowanhamwood.hungr.local.database.FavouriteRecipesDatabase
import org.rowanhamwood.hungr.local.database.RecipeDao
import java.io.IOException

@ExperimentalCoroutinesApi
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

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }




    @Test
    fun insertRecipeToDatabase()  = runTest {

        //Arrange
        localDataSource = LocalDataSource(recipeDao, ioDispatcher, fileSaverService)

        //Act and Assert
        assertThat(localDataSource.insertRecipe(recipeModel1),`is`(true))


    }

    @Test
    fun deleteRecipeFromDatabase()  = runTest {

        //Arrange
        localDataSource = LocalDataSource(recipeDao, ioDispatcher, fileSaverService)
        localDataSource.insertRecipe(recipeModel1)

        //Act and Assert
        localDataSource.deleteRecipe(databaseRecipe1)
        assert(!localDataSource.isRecipeSaved(databaseRecipe1.label))

    }






}