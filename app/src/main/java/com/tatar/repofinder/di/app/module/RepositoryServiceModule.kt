package com.tatar.repofinder.di.app.module

import com.tatar.repofinder.data.service.RepositoryService
import com.tatar.repofinder.di.app.scope.PerApp
import dagger.Module
import dagger.Provides

@Module
object RepositoryServiceModule {

    @JvmStatic
    @PerApp
    @Provides
    fun repositoryService(): RepositoryService {
        return RepositoryService()
    }
}
