package com.tatar.repofinder.di.app.module

import com.apollographql.apollo.ApolloClient
import com.tatar.repofinder.data.service.RepositoryService
import com.tatar.repofinder.di.app.scope.PerApp
import dagger.Module
import dagger.Provides

@Module(includes = [ApolloModule::class])
object RepositoryServiceModule {

    @JvmStatic
    @PerApp
    @Provides
    fun repositoryService(apolloClient: ApolloClient): RepositoryService {
        return RepositoryService(apolloClient)
    }
}
