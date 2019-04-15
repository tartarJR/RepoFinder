package com.tatar.repofinder.ui.search

import com.tatar.repofinder.data.model.Repository
import com.tatar.repofinder.data.service.RepositoryService
import com.tatar.repofinder.data.service.RepositoryService.OnFinishedListener
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import com.tatar.repofinder.util.ConnectionManager

class SearchPresenterImpl(
    private val searchView: SearchView,
    private val repositoryService: RepositoryService,
    private val connectionManager: ConnectionManager
) : SearchPresenter, OnFinishedListener {

    override fun performSearch(searchQuery: String) {
        if (searchQuery.isEmpty()) {
            searchView.displayEmptySearchQueryWarning()
        } else {
            if (!connectionManager.hasInternetConnection()) {
                searchView.displayNoInternetWarning()
            } else {
                searchView.activateProgressBar()
                repositoryService.getRepositoriesByQualifiersAndKeywords(searchQuery)
            }
        }
    }

    override fun onResponse(repositoryList: ArrayList<Repository>) {
        if (repositoryList.isEmpty()) {
            searchView.displayNotFoundMessage()
        } else {
            searchView.displayRepositories(repositoryList)
        }
    }

    override fun onError() {
        searchView.displayErrorMessage()
    }
}