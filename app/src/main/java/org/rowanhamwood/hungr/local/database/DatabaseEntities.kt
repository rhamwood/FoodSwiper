package org.rowanhamwood.hungr.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

import org.rowanhamwood.hungr.remote.network.RecipeModel


/**
     * DatabaseVideo represents a recipe entity in the database.
     */
    @Entity
    data class DatabaseRecipe constructor(
        @PrimaryKey
        val uri: String,
        val label: String,
        val image: String,
        val source: String,
        val url: String)

fun List<DatabaseRecipe>.asDomainModel(): List<RecipeModel> {
    return map {
        RecipeModel(
            uri = it.uri,
            label = it.label,
            image = it.image,
            source = it.source,
            url = it.url)
    }



}

