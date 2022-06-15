package org.rowanhamwood.hungr.remote.network


import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface RecipeApiService {
    @GET("recipes/v2")

    suspend fun getRecipes(
        @Query("type") type: String = "public",
        @Query("q") searchQuery: String,
        @Query("health") healthQuery: String?,
        @Query("cuisineType") cuisineQuery: String?,
        @Query("imageSize") imageSize: String = "LARGE",
        @Query("field") uri: String = "uri",
        @Query("field") label: String = "label",
        @Query("field") source: String = "source",
        @Query("field") url: String = "url",
        @Query("field") images: String = "images"

    )
            : RecipeData

    @GET
    suspend fun getNext(
        @Url url: String
    ): RecipeData
}







