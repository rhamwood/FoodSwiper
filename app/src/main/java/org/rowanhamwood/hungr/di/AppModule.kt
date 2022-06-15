package org.rowanhamwood.hungr.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.rowanhamwood.hungr.local.BaseLocalDataSource
import org.rowanhamwood.hungr.local.LocalDataSource
import org.rowanhamwood.hungr.local.database.FavouriteRecipesDatabase
import org.rowanhamwood.hungr.remote.BaseRemoteDataSource
import org.rowanhamwood.hungr.remote.RemoteDataSource
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.repository.RecipesRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideRemoteDataSource(
        database: FavouriteRecipesDatabase,
        ioDispatcher: CoroutineDispatcher
    ): BaseRemoteDataSource {
        return RemoteDataSource(
            database.getNextDao, ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        database: FavouriteRecipesDatabase,
        @ApplicationContext context: Context,
        ioDispatcher: CoroutineDispatcher
    ): BaseLocalDataSource {
        return LocalDataSource(
            database.recipeDao, context, ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): FavouriteRecipesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FavouriteRecipesDatabase::class.java,
            "recipes"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO


}

/**
 * The binding for TasksRepository is on its own module so that we can replace it easily in tests.
 */
@Module
@InstallIn(SingletonComponent::class)
object RecipesRepositoryModule {

    @Singleton
    @Provides
    fun provideRecipesRepository(
        remoteDataSource: BaseRemoteDataSource,
        localDataSource: BaseLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): BaseRecipesRepository {
        return RecipesRepository(
            localDataSource, remoteDataSource, ioDispatcher
        )
    }
}