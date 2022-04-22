package org.rowanhamwood.hungr.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {
    @Query("select * from DatabaseRecipe")
    fun getRecipes(): LiveData<List<DatabaseRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe( recipe: DatabaseRecipe)

    @Delete
    fun deleteRecipe(recipe: DatabaseRecipe)
}





    @Database(entities = [DatabaseRecipe::class], version = 1)
    abstract class FavouriteRecipesDatabase: RoomDatabase() {
        abstract val recipeDao: RecipeDao
    }


