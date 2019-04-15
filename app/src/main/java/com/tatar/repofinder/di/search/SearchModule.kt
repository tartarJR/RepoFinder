package com.tatar.repofinder.di.search

import com.tatar.repofinder.data.service.RepositoryService
import com.tatar.repofinder.ui.search.RepositoryAdapter
import com.tatar.repofinder.ui.search.RepositoryAdapter.ItemClickListener
import com.tatar.repofinder.ui.search.SearchActivity
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import com.tatar.repofinder.ui.search.SearchPresenterImpl
import com.tatar.repofinder.util.ConnectionManager
import dagger.Module
import dagger.Provides

@Module
class SearchModule(
    private val searchActivity: SearchActivity,
    private val searchView: SearchView,
    private val itemClickListener: ItemClickListener
) {

    @PerSearch
    @Provides
    fun searchActivity(): SearchActivity {
        return searchActivity
    }

    @PerSearch
    @Provides
    fun searchView(): SearchView {
        return searchView
    }

    @PerSearch
    @Provides
    fun itemClickListener(): ItemClickListener {
        return itemClickListener
    }

    @PerSearch
    @Provides
    fun searchPresenter(
        searchView: SearchView,
        repositoryService: RepositoryService,
        connectionManager: ConnectionManager
    ): SearchPresenter {
        return SearchPresenterImpl(searchView, repositoryService, connectionManager)
    }

    @PerSearch
    @Provides
    fun repositoryAdapter(itemClickListener: ItemClickListener): RepositoryAdapter {
        return RepositoryAdapter(itemClickListener)
    }

}