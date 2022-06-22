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
    private val ioDispatcher: CoroutineDispatcher,
    private val fileSaverService: BaseFileSaverService
) :
    BaseLocalDataSource {

    override suspend fun insertRecipe(favouriteRecipe: RecipeModel): Boolean = withContext(ioDispatcher){

            try {
                favouriteRecipe.smallImage = fileSaverService.imageUriToFile(favouriteRecipe.smallImage)
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
            fileSaverService.deleteImageFile(favouriteRecipe.image)
            recipeDao.deleteRecipe(favouriteRecipe)
        }
    }

    override fun getRecipes(): LiveData<Result<List<DatabaseRecipe>>> {
        return recipeDao.getRecipes().map { Result.Success(it) }

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




}