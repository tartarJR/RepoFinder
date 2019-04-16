package com.tatar.repofinder.ui.search

import com.tatar.repofinder.data.model.Repository
import com.tatar.repofinder.data.service.RepositoryService
import com.tatar.repofinder.data.service.RepositoryService.OnFinishedListener
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import com.tatar.repofinder.util.ConnectionManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class SearchPresenterImpl(
    private val repositoryService: RepositoryService,
    private val connectionManager: ConnectionManager
) : SearchPresenter, OnFinishedListener {

    private val logger = AnkoLogger(SearchPresenterImpl::class.java)

    private var searchView: SearchView? = null

    override fun performSearch(searchQuery: String) {
        if (!connectionManager.hasInternetConnection()) {
            searchView?.displayNoInternetWarning() ?: printDetachedViewErrorLog()
        } else {
            if (searchQuery.isEmpty()) {
                searchView?.displayEmptyQueryStringToast() ?: printDetachedViewErrorLog()
            } else {
                searchView?.disableSearchButton() ?: printDetachedViewErrorLog()
                searchView?.hideResultContent() ?: printDetachedViewErrorLog()
                searchView?.showProgressBar() ?: printDetachedViewErrorLog()
                searchView?.displaySearchingMessage() ?: printDetachedViewErrorLog()
                searchView?.showStatusTv() ?: printDetachedViewErrorLog()

                repositoryService.getRepositoriesByQualifiersAndKeywords(searchQuery, this)
            }
        }
    }

    override fun onRepositoryItemClick(repositoryName: String, repositoryOwnerName: String) {
        searchView?.startDetailActivity(repositoryName, repositoryOwnerName)
    }

    override fun onResponse(repositoryList: ArrayList<Repository>) {
        searchView?.enableSearchButton() ?: printDetachedViewErrorLog()
        searchView?.hideProgressBar() ?: printDetachedViewErrorLog()

        if (repositoryList.isEmpty()) {
            searchView?.displayNoRepositoriesFoundMessage() ?: printDetachedViewErrorLog()
        } else {
            searchView?.hideKeyboard()
            searchView?.showResultContent(repositoryList) ?: printDetachedViewErrorLog()
            searchView?.hideStatusTv() ?: printDetachedViewErrorLog()
        }
    }

    override fun onError() {
        searchView?.enableSearchButton() ?: printDetachedViewErrorLog()
        searchView?.hideResultContent() ?: printDetachedViewErrorLog()
        searchView?.hideProgressBar() ?: printDetachedViewErrorLog()
        searchView?.displayErrorMessage() ?: printDetachedViewErrorLog()
        searchView?.showStatusTv() ?: printDetachedViewErrorLog()
    }

    override fun attach(searchView: SearchView?) {
        this.searchView = searchView
    }

    override fun detach() {
        this.searchView = null
    }

    private fun printDetachedViewErrorLog() {
        logger.error("View is detached")
    }
}