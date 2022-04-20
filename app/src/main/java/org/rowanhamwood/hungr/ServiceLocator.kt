package org.rowanhamwood.hungr

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import kotlinx.coroutines.runBlocking
import org.rowanhamwood.hungr.database.FavouriteRecipesDatabase
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

    fun provideRecipesRespository (context: Context) : BaseRecipesRepository {
        synchronized(this) {
            return baseRecipesRepository?: createRecipesRepository(context)
        }
    }

    private fun createRecipesRepository (context: Context) : RecipesRepository {
        val database = INSTANCE ?: createDatabase(context)
        val newRepo = RecipesRepository(database.recipeDao)
        baseRecipesRepository = newRepo
        return newRepo
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

