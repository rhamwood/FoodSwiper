package org.rowanhamwood.hungr.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.rowanhamwood.hungr.data.source.AndroidFakeTestRepository
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RecipesRepositoryModule::class]
)
abstract class TestRecipesRepositoryModule {
    @Singleton
    @Binds
    abstract fun provideRecipesRepository(repo: AndroidFakeTestRepository): BaseRecipesRepository
}
