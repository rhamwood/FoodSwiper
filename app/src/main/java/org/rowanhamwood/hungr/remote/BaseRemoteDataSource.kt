package org.rowanhamwood.hungr.remote

import androidx.lifecycle.LiveData
import org.rowanhamwood.hungr.remote.network.RecipeModel

interface BaseRemoteDataSource {

    val recipes: LiveData<List<RecipeModel>>

    suspend fun getRecipes(searchQuery: String, healthQuery: String?, cuisineQuery: String?)
    suspend fun getNext()
}