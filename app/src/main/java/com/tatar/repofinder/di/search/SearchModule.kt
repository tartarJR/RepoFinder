package com.tatar.repofinder.di.search

import com.tatar.repofinder.data.service.RepositoryService
import com.tatar.repofinder.ui.search.RepositoryAdapter
import com.tatar.repofinder.ui.search.RepositoryAdapter.ItemClickListener
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import com.tatar.repofinder.ui.search.SearchPresenterImpl
import com.tatar.repofinder.util.ConnectionManager
import dagger.Module
import dagger.Provides

@Module
object SearchModule {

    @JvmStatic
    @PerSearch
    @Provides
    fun searchPresenter(
        searchView: SearchView,
        repositoryService: RepositoryService,
        connectionManager: ConnectionManager
    ): SearchPresenter {
        return SearchPresenterImpl(searchView, repositoryService, connectionManager)
    }

    @JvmStatic
    @PerSearch
    @Provides
    fun repositoryAdapter(itemClickListener: ItemClickListener): RepositoryAdapter {
        return RepositoryAdapter(itemClickListener)
    }
}
