package com.tatar.repofinder.di.detail

import com.squareup.picasso.Picasso
import com.tatar.repofinder.data.service.RepoService
import com.tatar.repofinder.di.search.PerSearch
import com.tatar.repofinder.ui.detail.DetailContract
import com.tatar.repofinder.ui.detail.DetailPresenterImpl
import com.tatar.repofinder.ui.detail.SubscriberAdapter
import com.tatar.repofinder.util.ConnectionManager
import dagger.Module
import dagger.Provides

@Module
object DetailModule {

    @JvmStatic
    @PerSearch
    @Provides
    fun detailPresenter(
        repoService: RepoService,
        connectionManager: ConnectionManager
    ): DetailContract.DetailPresenter {
        return DetailPresenterImpl(repoService, connectionManager)
    }

    @JvmStatic
    @PerSearch
    @Provides
    fun subscriberAdapter(picasso: Picasso): SubscriberAdapter {
        return SubscriberAdapter(picasso)
    }
}