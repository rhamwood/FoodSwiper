package org.rowanhamwood.hungr.data.source

import org.rowanhamwood.hungr.local.BaseFileSaverService



class FakeFileSaverService: BaseFileSaverService {

    lateinit var image: String
    override suspend fun imageUriToFile(imgUri: String): String {
        return "https://upload.wikimedia.org/wikipedia/commons/8/84/Coconut_cream_pie.jpg"
    }



    override suspend fun deleteImageFile(imgPath: String) {
        image = imgPath
    }
}