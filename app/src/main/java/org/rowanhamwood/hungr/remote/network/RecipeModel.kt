package org.rowanhamwood.hungr.remote.network

data class RecipeModel(
    val uri: String,
    val label: String,
    val largeImage: String,
    var smallImage: String,
    val source: String,
    val url: String,

    )
