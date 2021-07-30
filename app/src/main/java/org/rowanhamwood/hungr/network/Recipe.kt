package org.rowanhamwood.hungr.network

import com.squareup.moshi.Json


data class Recipe(
    val id: Int,
    val title: String,
    val imageType: String
    )
