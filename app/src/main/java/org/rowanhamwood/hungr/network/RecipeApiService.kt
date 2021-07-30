package org.rowanhamwood.hungr.network

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.spoonacular.com/recipes/"
private const val API_KEY = "41c3f4042c90429299a6d6740b34a351"

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
        .addQueryParameter("apiKey", API_KEY).build()


    request.url(newUrl)
    return chain.proceed(request.build())
}

interface RecipeApiService {
    @GET("complexSearch")
    suspend fun getRecipes(
        @Query("query") searchQuery: String,
        @Query("number") numberQuery: Int,
        @Query("cuisine") cuisineQuery: String?,
        @Query("diet") dietQuery: String?
    )
            : RecipeList
}

//@Query("apiKey") apiKey: String,

object RecipeApi {

    val retrofitService: RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }
}




