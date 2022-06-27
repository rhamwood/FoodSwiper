package org.rowanhamwood.hungr.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FileSaverService(private val context: Context) : BaseFileSaverService {

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

    private fun recipeImageDirectory(): String = context.filesDir.absolutePath

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

    override suspend fun imageUriToFile(imgUri: String): String {

        val recipeBitmap = getBitmapFile(imgUri)
        val imageId = UUID.randomUUID().toString()
        saveFavouriteRecipeFile(recipeBitmap, imageId)

        val path = File(recipeImageDirectory(), imageId).absolutePath

        return path

    }

    override suspend fun deleteImageFile(imgPath: String) {
        File(imgPath).delete()
    }

}