package com.tatar.repofinder.di.app.module

import com.apollographql.apollo.ApolloClient
import com.tatar.repofinder.data.service.RepoService
import com.tatar.repofinder.di.app.scope.PerApp
import dagger.Module
import dagger.Provides

@Module(includes = [ApolloModule::class])
object RepoServiceModule {

    @JvmStatic
    @PerApp
    @Provides
    fun repoService(apolloClient: ApolloClient): RepoService {
        return RepoService(apolloClient)
    }
}
