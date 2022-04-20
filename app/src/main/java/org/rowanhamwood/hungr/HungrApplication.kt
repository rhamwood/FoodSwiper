package org.rowanhamwood.hungr

import android.app.Application
import org.rowanhamwood.hungr.BuildConfig
import org.rowanhamwood.hungr.repository.BaseRecipesRepository

class HungrApplication : Application() {

    val recipesRepository: BaseRecipesRepository
        get() = ServiceLocator.provideRecipesRespository(this)

}