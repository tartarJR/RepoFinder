package com.tatar.repofinder.ui.detail

import com.tatar.repofinder.data.model.Repository
import com.tatar.repofinder.data.service.RepositoryService
import com.tatar.repofinder.ui.detail.DetailContract.DetailPresenter
import com.tatar.repofinder.ui.detail.DetailContract.DetailView
import com.tatar.repofinder.ui.search.SearchPresenterImpl
import com.tatar.repofinder.util.ConnectionManager
import org.jetbrains.anko.AnkoLogger

class DetailPresenterImpl(
    private val repositoryService: RepositoryService,
    private val connectionManager: ConnectionManager
) : DetailPresenter, RepositoryService.OnFinishedListener {

    private val logger = AnkoLogger(SearchPresenterImpl::class.java)

    private var detailView: DetailView? = null

    override fun getRepositoryDetails() {

    }

    override fun attach(view: DetailView?) {
        this.detailView = view
    }

    override fun detach() {
        this.detailView = null
    }

    override fun onResponse(repositoryList: ArrayList<Repository>) {

    }

    override fun onError() {

    }

}