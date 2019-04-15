package com.tatar.repofinder.di.search

import com.tatar.repofinder.di.app.component.AppComponent
import com.tatar.repofinder.ui.search.SearchActivity
import dagger.Component

@PerSearch
@Component(modules = [SearchModule::class], dependencies = [AppComponent::class])
interface SearchComponent {
    fun injectSearchActivity(searchActivity: SearchActivity)
}