package org.rowanhamwood.hungr.local

interface BaseFileSaverService {
    suspend fun imageUriToFile(imgUri: String): String
    suspend fun deleteImageFile(imgPath: String)
}