package com.tatar.repofinder.di.search

import com.tatar.repofinder.di.app.component.AppComponent
import com.tatar.repofinder.ui.search.RepoAdapter.ItemClickListener
import com.tatar.repofinder.ui.search.SearchActivity
import dagger.BindsInstance
import dagger.Component

@PerSearch
@Component(modules = [SearchModule::class], dependencies = [AppComponent::class])
interface SearchComponent {

    fun injectSearchActivity(searchActivity: SearchActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun searchActivity(searchActivity: SearchActivity): Builder

        @BindsInstance
        fun itemClickListener(itemClickListener: ItemClickListener): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): SearchComponent
    }
}