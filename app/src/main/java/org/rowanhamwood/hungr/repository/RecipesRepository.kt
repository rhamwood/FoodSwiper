package org.rowanhamwood.hungr.repository

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.LiveData
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.BaseRemoteDataSource
import org.rowanhamwood.hungr.remote.network.RecipeModel
import java.io.File
import java.io.FileOutputStream
import java.util.*


private const val TAG = "RecipesRepository"


class RecipesRepository(
    private val baseLocalDataSource: BaseLocalDataSource,
    private val baseRemoteDataSource: BaseRemoteDataSource,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

) : BaseRecipesRepository {

    override val favouriteRecipes: LiveData<Result<List<DatabaseRecipe>>> = baseLocalDataSource.getRecipes()


    override suspend fun getRecipes(searchQuery: String?, healthQuery: String?, cuisineQuery: String?, getNext: Boolean, appNewStart: Boolean) : Result<LiveData<List<RecipeModel>>> {
        return baseRemoteDataSource.getRecipes(searchQuery, healthQuery, cuisineQuery, getNext, appNewStart)
    }


     override suspend fun insertRecipe(favouriteRecipe: RecipeModel) {
         val recipeBitmap = getBitmapFile(favouriteRecipe.image)
         val imageId = UUID.randomUUID().toString()
         saveFavouriteRecipeFile(recipeBitmap, imageId)

         val file = File(recipeImageDirectory(), imageId)
         val path  = file.absolutePath
         favouriteRecipe.image = path

         val databaseRecipe = maptoDataBaseModel(favouriteRecipe)
         withContext(ioDispatcher) {
             baseLocalDataSource.insertRecipe(databaseRecipe)
         }
     }

    override suspend fun deleteRecipe(favouriteRecipe: RecipeModel) {
        val databaseRecipe = maptoDataBaseModel(favouriteRecipe)
        withContext(ioDispatcher) {
            baseLocalDataSource.deleteRecipe(databaseRecipe)
        }
    }

    private suspend fun getBitmapFile(imgUrl: String) : Bitmap = withContext(ioDispatcher){

        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imgUrl)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap

        return@withContext bitmap
    }


    private fun saveFavouriteRecipeFile(recipeBitmap: Bitmap, fileName: String ){
        try {
            val fileOutputStream: FileOutputStream =
                context.openFileOutput(fileName, Context.MODE_PRIVATE)
            recipeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }






    private fun recipeImageFile(fileName: String): File = File(recipeImageDirectory(), fileName)

    private fun recipeImageDirectory(): String = context.filesDir.absolutePath


    private fun maptoDataBaseModel(recipeModel: RecipeModel): DatabaseRecipe {
        return recipeModel.let {
            DatabaseRecipe(
                uri = it.uri,
                label = it.label,
                image = it.image,
                source = it.source,
                url = it.url
            )
        }
    }




}



