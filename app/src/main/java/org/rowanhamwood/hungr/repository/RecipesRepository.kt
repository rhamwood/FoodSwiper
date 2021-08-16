package org.rowanhamwood.hungr.repository

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Transformations
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import org.rowanhamwood.hungr.database.DatabaseRecipe
//import org.rowanhamwood.hungr.database.FavouriteRecipesDatabase
//import org.rowanhamwood.hungr.database.asDomainModel
//import org.rowanhamwood.hungr.network.RecipeModel
//
//
//class RecipesRepository (private val database: FavouriteRecipesDatabase){
//
//    val recipes: LiveData<List<RecipeModel>> = Transformations.map(database.recipeDao.getRecipes()){
//        it.asDomainModel()
//    }
//
//
//
//    suspend fun insertRecipes(favouriteRecipe: DatabaseRecipe) {
//        withContext(Dispatchers.IO) {
//            database.recipeDao.insertRecipe(favouriteRecipe)
//        }
//    }
//}



//}