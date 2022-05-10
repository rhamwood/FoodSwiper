package org.rowanhamwood.hungr.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

import org.rowanhamwood.hungr.remote.network.RecipeModel


/**
     * DatabaseRecipe represents a recipe entity in the database.
     */
    @Entity
    data class DatabaseRecipe constructor(
        @PrimaryKey
        val uri: String,
        val label: String,
        val image: String,
        val source: String,
        val url: String)

        @Entity
        data class getNextUrl constructor(
            @PrimaryKey val getNextId: String,
            val nextUrl: String,

        )



//fun List<DatabaseRecipe>.asDomainModel(): List<RecipeModel> {
//    return map {
//        RecipeModel(
//            uri = it.uri,
//            label = it.label,
//            smallImage = it.image,
//            source = it.source,
//            url = it.url)
//    }
//
//
//
//}

