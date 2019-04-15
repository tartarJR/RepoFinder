package com.tatar.repofinder.di.app.module

import com.apollographql.apollo.ApolloClient
import com.tatar.repofinder.di.app.scope.PerApp
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module(includes = [NetworkModule::class])
object ApolloModule {
    private const val GITHUB_API_BASE_URL = "https://api.github.com/graphql"

    @JvmStatic
    @PerApp
    @Provides
    fun apolloClient(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.builder()
            .serverUrl(GITHUB_API_BASE_URL)
            .okHttpClient(okHttpClient)
            .build()
    }
}
