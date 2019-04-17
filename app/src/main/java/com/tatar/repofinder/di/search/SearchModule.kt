package com.tatar.repofinder.di.search

import com.squareup.picasso.Picasso
import com.tatar.repofinder.data.service.RepoService
import com.tatar.repofinder.ui.search.RepoAdapter
import com.tatar.repofinder.ui.search.RepoAdapter.ItemClickListener
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
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
        repoService: RepoService,
        connectionManager: ConnectionManager
    ): SearchPresenter {
        return SearchPresenterImpl(repoService, connectionManager)
    }

    @JvmStatic
    @PerSearch
    @Provides
    fun repoAdapter(picasso: Picasso, itemClickListener: ItemClickListener): RepoAdapter {
        return RepoAdapter(picasso, itemClickListener)
    }
}
