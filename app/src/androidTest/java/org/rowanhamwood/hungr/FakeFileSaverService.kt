package org.rowanhamwood.hungr

import org.rowanhamwood.hungr.local.BaseFileSaverService



class FakeFileSaverService: BaseFileSaverService {

    lateinit var image: String
    override suspend fun imageUriToFile(imgUri: String): String {
        return "smallImagePath"
    }



    override suspend fun deleteImageFile(imgPath: String) {
        image = imgPath
    }
}