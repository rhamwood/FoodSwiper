package org.rowanhamwood.hungr

import kotlinx.coroutines.runBlocking
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel
import org.rowanhamwood.hungr.repository.BaseRecipesRepository

fun BaseRecipesRepository.getRecipesBlocking(searchQuery: String?,
                                             healthQuery: String?,
                                             cuisineQuery: String?,
                                             getNext: Boolean,
                                             appNewStart: Boolean  ) = runBlocking {
    this@getRecipesBlocking.getRecipes(searchQuery, healthQuery, cuisineQuery, getNext, appNewStart )
}

fun BaseRecipesRepository.insertRecipeBlocking(favouriteRecipe: RecipeModel) : Boolean = runBlocking{
    this@insertRecipeBlocking.insertRecipe(favouriteRecipe)
}

fun BaseRecipesRepository.deleteRecipeBlocking(favouriteRecipe: DatabaseRecipe) = runBlocking{
    this@deleteRecipeBlocking.deleteRecipe(favouriteRecipe)
}

