package org.rowanhamwood.hungr.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {
    @Query("select * from DatabaseRecipe")
    fun getRecipes(): LiveData<List<DatabaseRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: DatabaseRecipe)

    @Delete
    fun deleteRecipe(recipe: DatabaseRecipe)
}

@Dao
interface getNextDao {
    @Query("SElECT * FROM getNextUrl WHERE getNextId = :getNextId")
    fun getNextById(getNextId: String): getNextUrl

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGetNext(getNextUrl: getNextUrl)

    @Delete
    fun deleteGetNext(getNextUrl: getNextUrl)
}


@Database(
    entities = [DatabaseRecipe::class, getNextUrl::class],
    version = 1,
    exportSchema = false


)


abstract class FavouriteRecipesDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
    abstract val getNextDao: getNextDao
}


