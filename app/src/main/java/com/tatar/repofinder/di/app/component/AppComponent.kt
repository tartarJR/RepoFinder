package com.tatar.repofinder.di.app.component

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.squareup.picasso.Picasso
import com.tatar.repofinder.data.service.RepositoryService
import com.tatar.repofinder.di.app.module.ApolloModule
import com.tatar.repofinder.di.app.module.PicassoModule
import com.tatar.repofinder.di.app.module.RepositoryServiceModule
import com.tatar.repofinder.di.app.scope.PerApp
import com.tatar.repofinder.util.ConnectionManager
import dagger.BindsInstance
import dagger.Component

@PerApp
@Component(modules = [ApolloModule::class, PicassoModule::class, RepositoryServiceModule::class])
interface AppComponent {
    fun apolloClient(): ApolloClient
    fun picasso(): Picasso
    fun repositoryService(): RepositoryService
    fun connectionManager(): ConnectionManager

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): AppComponent
    }
}