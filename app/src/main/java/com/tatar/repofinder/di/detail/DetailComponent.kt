package com.tatar.repofinder.di.detail

import com.tatar.repofinder.di.app.component.AppComponent
import com.tatar.repofinder.di.search.PerSearch
import com.tatar.repofinder.ui.detail.DetailActivity
import dagger.BindsInstance
import dagger.Component

@PerSearch
@Component(modules = [DetailModule::class], dependencies = [AppComponent::class])
interface DetailComponent {

    fun injectDetailActivity(detailActivity: DetailActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun detailActivity(detailActivity: DetailActivity): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): DetailComponent
    }
}