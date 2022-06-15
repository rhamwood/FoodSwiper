package org.rowanhamwood.hungr.local


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.rowanhamwood.hungr.Result
import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.local.database.RecipeDao
import org.rowanhamwood.hungr.remote.network.RecipeModel
import java.io.File
import java.io.FileOutputStream
import java.util.*


class LocalDataSource(
    private val recipeDao: RecipeDao,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) :
    BaseLocalDataSource {

    override suspend fun insertRecipe(favouriteRecipe: RecipeModel): Boolean = withContext(ioDispatcher){

            try {
                val recipeBitmap = getBitmapFile(favouriteRecipe.smallImage)
                val imageId = UUID.randomUUID().toString()
                saveFavouriteRecipeFile(recipeBitmap, imageId)

                val path = File(recipeImageDirectory(), imageId).absolutePath
                favouriteRecipe.smallImage = path

                val databaseRecipe = maptoDataBaseModel(favouriteRecipe)
                recipeDao.insertRecipe(databaseRecipe)
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }

    }

    override suspend fun deleteRecipe(favouriteRecipe: DatabaseRecipe) {
        withContext(ioDispatcher) {
            File(favouriteRecipe.image).delete()
            recipeDao.deleteRecipe(favouriteRecipe)
        }
    }

    override fun getRecipes(): LiveData<Result<List<DatabaseRecipe>>> {
        return recipeDao.getRecipes().map { Result.Success(it) }

    }


    private suspend fun getBitmapFile(imgUrl: String): Bitmap {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imgUrl)
            .allowHardware(false)
            // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap

        return bitmap
    }


    private fun saveFavouriteRecipeFile(recipeBitmap: Bitmap, fileName: String) {
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
                image = it.smallImage,
                source = it.source,
                url = it.url
            )
        }
    }

    private fun recipeImageDirectory(): String = context.filesDir.absolutePath


}