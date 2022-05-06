package org.rowanhamwood.hungr.repository

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

     private fun recipeImageDirectory(): String = context.filesDir.absolutePath

     override suspend fun insertRecipe(favouriteRecipe: RecipeModel) = withContext(ioDispatcher) {

             val recipeBitmap = getBitmapFile(favouriteRecipe.image)
             val imageId = UUID.randomUUID().toString()
             saveFavouriteRecipeFile(recipeBitmap, imageId)

             val path = File(recipeImageDirectory(), imageId).absolutePath
             favouriteRecipe.image = path

             val databaseRecipe = maptoDataBaseModel(favouriteRecipe)
             baseLocalDataSource.insertRecipe(databaseRecipe)

     }



    override suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            File(favouriteRecipe.image).delete()
            baseLocalDataSource.deleteRecipe(favouriteRecipe)
        }
    }

    private suspend fun getBitmapFile(imgUrl: String) : Bitmap {

        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imgUrl)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap

        return bitmap
    }


    private fun saveFavouriteRecipeFile(recipeBitmap: Bitmap, fileName: String )  {
        try {
            val fileOutputStream: FileOutputStream =
                context.openFileOutput(fileName, Context.MODE_PRIVATE)
            recipeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }








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



