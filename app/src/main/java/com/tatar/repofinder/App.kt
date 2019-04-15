package com.tatar.repofinder

import android.app.Application
import com.tatar.repofinder.di.app.component.AppComponent
import com.tatar.repofinder.di.app.component.DaggerAppComponent
import com.tatar.repofinder.di.app.module.AppModule

class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        instance = this
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    fun appComponent(): AppComponent {
        return appComponent
    }

    companion object {
        lateinit var instance: App private set
    }

}