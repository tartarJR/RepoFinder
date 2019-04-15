package com.tatar.repofinder.di.app.module

import android.content.Context
import com.tatar.repofinder.di.app.scope.PerApp
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @PerApp
    @Provides
    fun context(): Context {
        return context.applicationContext
    }
}