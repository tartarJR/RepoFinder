package com.tatar.repofinder.di.app.component

import com.apollographql.apollo.ApolloClient
import com.squareup.picasso.Picasso
import com.tatar.repofinder.di.app.module.ApolloModule
import com.tatar.repofinder.di.app.module.PicassoModule
import com.tatar.repofinder.di.app.scope.PerApp
import dagger.Component

@PerApp
@Component(modules = [ApolloModule::class, PicassoModule::class])
interface AppComponent {
    fun apolloClient(): ApolloClient
    fun picasso(): Picasso
}