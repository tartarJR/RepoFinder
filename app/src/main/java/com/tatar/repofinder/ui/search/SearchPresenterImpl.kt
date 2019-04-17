package com.tatar.repofinder.ui.search

import com.tatar.repofinder.data.model.Repo
import com.tatar.repofinder.data.service.RepoService
import com.tatar.repofinder.data.service.RepoServiceResponse
import com.tatar.repofinder.data.service.RepoServiceListener
import com.tatar.repofinder.ui.base.BaseContract.BasePresenter.Companion.DETACHED_VIEW_ERROR
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import com.tatar.repofinder.util.ConnectionManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

// TODO I don't like this if else check for the view
class SearchPresenterImpl(
    private val repoService: RepoService,
    private val connectionManager: ConnectionManager
) : SearchPresenter, RepoServiceListener<Repo> {

    private val logger = AnkoLogger(SearchPresenterImpl::class.java)

    private var searchView: SearchView? = null

    override fun performSearch(searchQuery: String) {
        if (this.searchView != null) {
            if (!connectionManager.hasInternetConnection()) {
                searchView?.displayNoInternetWarning()
            } else {
                if (searchQuery.isEmpty()) {
                    searchView?.displayEmptyQueryStringToast()
                } else {
                    searchView?.disableSearchButton()
                    searchView?.hideResultContent()
                    searchView?.showProgressBar()
                    searchView?.displaySearchingMessage()
                    searchView?.showStatusTv()

                    repoService.getRepositoriesByQualifiersAndKeywords(searchQuery, this)
                }
            }
        } else {
            logger.error(DETACHED_VIEW_ERROR)
        }
    }

    override fun onRepositoryItemClick(repositoryName: String, repositoryOwnerName: String) {
        if (this.searchView != null) {
            searchView?.startDetailActivity(repositoryName, repositoryOwnerName)
        } else {
            logger.error(DETACHED_VIEW_ERROR)
        }
    }

    override fun onResponse(repoServiceResponse: RepoServiceResponse<Repo>) {
        if (this.searchView != null) {
            searchView?.enableSearchButton()
            searchView?.hideProgressBar()

            val repositories = repoServiceResponse.items

            if (repositories.isEmpty()) {
                searchView?.displayNoRepositoriesFoundMessage()
            } else {
                searchView?.hideStatusTv()
                searchView?.hideKeyboard()
                searchView?.showResultContent(repositories)
            }
        } else {
            logger.error(DETACHED_VIEW_ERROR)
        }
    }

    override fun onError() {
        if (this.searchView != null) {
            searchView?.enableSearchButton()
            searchView?.hideResultContent()
            searchView?.hideProgressBar()
            searchView?.displayErrorMessage()
            searchView?.showStatusTv()
        } else {
            logger.error(DETACHED_VIEW_ERROR)
        }
    }

    override fun attach(view: SearchView?) {
        this.searchView = view
    }

    override fun detach() {
        this.searchView = null
    }
}