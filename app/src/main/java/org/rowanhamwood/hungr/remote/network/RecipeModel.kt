package org.rowanhamwood.hungr.remote.network

data class RecipeModel (
    val uri: String,
    val label: String,
    var image: String,
    val source: String,
    val url: String
        )
