package org.rowanhamwood.hungr

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp
import org.rowanhamwood.hungr.di.AppModule.provideSharedPreferences

import org.rowanhamwood.hungr.repository.BaseRecipesRepository

@HiltAndroidApp
class HungrApplication : Application() {


}