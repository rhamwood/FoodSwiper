package org.rowanhamwood.hungr

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.Room
import kotlinx.coroutines.runBlocking
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.LocalDataSource
import org.rowanhamwood.hungr.local.database.FavouriteRecipesDatabase
import org.rowanhamwood.hungr.remote.BaseRemoteDataSource
import org.rowanhamwood.hungr.remote.RemoteDataSource
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.repository.RecipesRepository


object ServiceLocator {

    private  var INSTANCE: FavouriteRecipesDatabase? = null

    @Volatile
    var baseRecipesRepository: BaseRecipesRepository? = null
        @VisibleForTesting set

    private val lock = Any()

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
//                TasksRemoteDataSource.deleteAllTasks()
           }
            // Clear all data to avoid test pollution.
            INSTANCE?.apply {
                clearAllTables()
                close()
            }
            INSTANCE = null
            baseRecipesRepository = null
        }
    }

    fun provideSharedPreferences(context: Context, preferenceFileKey: String) : SharedPreferences {
        val sharedPreferences = context.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE)
        return sharedPreferences
    }


    fun provideRecipesRespository (context: Context) : BaseRecipesRepository {
        synchronized(this) {
            return baseRecipesRepository?: createRecipesRepository(context)
        }
    }

    private fun createRecipesRepository (context: Context) : RecipesRepository {
        val newRepo = RecipesRepository(createLocalDatasource(context), createRemoteDataSource(context), context)
        baseRecipesRepository = newRepo
        return newRepo
    }

    private fun createLocalDatasource (context: Context) : BaseLocalDataSource{
        val database = INSTANCE ?: createDatabase(context)
        return LocalDataSource(database.recipeDao)
    }

    private fun createRemoteDataSource (context: Context) : BaseRemoteDataSource{
        val database = INSTANCE ?: createDatabase(context)
        return RemoteDataSource(database.getNextDao)
    }


    fun createDatabase(context: Context): FavouriteRecipesDatabase {
        synchronized(FavouriteRecipesDatabase::class.java) {

           val result = Room.databaseBuilder(context.applicationContext,
                    FavouriteRecipesDatabase::class.java,
                    "recipes").build()
           INSTANCE = result
            return result
        }

    }
}

