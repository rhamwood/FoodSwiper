package org.rowanhamwood.hungr.remote.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url



private const val BASE_URL = "https://api.edamam.com/api/"
private const val API_KEY = "801fbc496f6b39e5afbe4b810261585f"
private const val APP_ID = "2023fe67"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val client = OkHttpClient.Builder()
    .addInterceptor { chain -> return@addInterceptor addApiKeyToRequests(chain) }
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()


private fun addApiKeyToRequests(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
    val originalHttpUrl = chain.request().url()
    val newUrl = originalHttpUrl.newBuilder()
        .addQueryParameter("app_id", APP_ID)
        .addQueryParameter("app_key", API_KEY)
        .build()
    request.url(newUrl)
    return chain.proceed(request.build())
}


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


object RecipeApi {
    val retrofitService: RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }
}




