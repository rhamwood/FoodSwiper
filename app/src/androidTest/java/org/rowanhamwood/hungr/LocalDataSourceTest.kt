package org.rowanhamwood.hungr

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.LocalDataSource
import org.rowanhamwood.hungr.local.database.FavouriteRecipesDatabase
import org.rowanhamwood.hungr.local.database.RecipeDao
import java.io.IOException

class LocalDataSourceTest {
    private lateinit var recipeDao: RecipeDao
    private lateinit var database : FavouriteRecipesDatabase
    private lateinit var context: Context
    private lateinit var ioDispatcher: CoroutineDispatcher
    private lateinit var localDataSource: BaseLocalDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDbAndLocalDatasource() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, FavouriteRecipesDatabase::class.java)
            .build()
        recipeDao = database.recipeDao
        ioDispatcher = Dispatchers.Main











        localDataSource = LocalDataSource(recipeDao ,context, ioDispatcher)

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }



}