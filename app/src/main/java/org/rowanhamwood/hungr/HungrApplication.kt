package org.rowanhamwood.hungr

import android.app.Application
import android.content.SharedPreferences

import org.rowanhamwood.hungr.repository.BaseRecipesRepository

class HungrApplication : Application() {

    val recipesRepository: BaseRecipesRepository
        get() = ServiceLocator.provideRecipesRespository(this)

    val sharedPreferences: SharedPreferences
        get() = ServiceLocator.provideSharedPreferences(
            this,
            getString(R.string.preference_file_key)
        )

}