package org.rowanhamwood.hungr.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.viewpager.widget.ViewPager

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


