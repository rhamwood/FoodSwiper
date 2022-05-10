package org.rowanhamwood.hungr.remote.network

data class RecipeModel (
    val uri: String,
    val label: String,
    var largeImage: String,
    val smallImage: String,
    val source: String,
    val url: String,

    )
