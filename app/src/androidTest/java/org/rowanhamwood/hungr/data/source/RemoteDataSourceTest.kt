package org.rowanhamwood.hungr.data.source


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.rowanhamwood.hungr.*
import org.rowanhamwood.hungr.local.database.FavouriteRecipesDatabase
import org.rowanhamwood.hungr.local.database.getNextDao
import org.rowanhamwood.hungr.remote.RemoteDataSource
import org.rowanhamwood.hungr.remote.network.RecipeApiService
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException


private const val TAG = "RemoteDataSourceTest"

@ExperimentalCoroutinesApi
class RemoteDataSourceTest {

    private lateinit var context: Context
    private lateinit var getNextDao: getNextDao
    private lateinit var database : FavouriteRecipesDatabase
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: RecipeApiService
    private lateinit var ioDispatcher: CoroutineDispatcher


    private val client = OkHttpClient.Builder().build()

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = AndroidCoroutineRule()



    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, FavouriteRecipesDatabase::class.java)
            .build()
        getNextDao = database.getNextDao
        ioDispatcher = Dispatchers.Main


    }



    @Before
    fun createServer() {
        mockWebServer = MockWebServer()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // setting a dummy url
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
            .create(RecipeApiService::class.java)


    }

    @After
    fun shutdownServer() {
        mockWebServer.shutdown()
    }



    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }


    @Test
    fun correctResponseIsParsedIntoSuccessResult() = runTest {
        // Arrange
        val response = MockResponse()
            .setBody(successfulResponse)
            .setResponseCode(200)

        mockWebServer.enqueue(response)

        val remoteDataSource = RemoteDataSource(getNextDao, ioDispatcher, apiService)
        val expectedRecipesList = responseRecipeList

        // Act
        val result = remoteDataSource
            .getRecipes("PIE", null, null, false, false )



        // Assert
        assert(result is Result.Success)
        assertEquals((result as Result.Success).data.getOrAwaitValueAndroidTest(), expectedRecipesList)
    }

    @Test
    fun malformedResponseReturnsJsonErroResult() = runTest {
        // Arrange
        val response = MockResponse()
            .setBody(errorResponse)
            .setResponseCode(200)

        mockWebServer.enqueue(response)

        val remoteDataSource = RemoteDataSource(getNextDao, ioDispatcher, apiService)

        // Act
        val result = remoteDataSource
            .getRecipes("PIE", null, null, false, false)

        // Assert
        assert(result is Result.Error)
        assert((result as Result.Error).exception is JsonDataException)
    }

    @Test
    fun errorResponseReturnsHttpErrorResult() = runTest {
        // Arrange
        val response = MockResponse()
            .setBody(errorResponse)
            .setResponseCode(400)

        mockWebServer.enqueue(response)

        val remoteDataSource = RemoteDataSource(getNextDao, ioDispatcher, apiService)

        // Act
        val result = remoteDataSource
            .getRecipes("PIE", null, null, false, false)

        // Assert
        assert(result is Result.Error)
        assert((result as Result.Error).exception is HttpException)
    }

}