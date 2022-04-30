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
    fun getNextById(getNextId: Int): getNextUrl

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGetNext(getNextUrl: getNextUrl)

    @Delete
    fun deleteGetNext(getNextUrl: getNextUrl)
}


@Database(
    entities = [DatabaseRecipe::class, getNextUrl::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)


    ]
)




abstract class FavouriteRecipesDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
    abstract val getNextDao: getNextDao
}


