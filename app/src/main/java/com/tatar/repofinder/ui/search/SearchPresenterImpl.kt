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
        if (!connectionManager.hasInternetConnection()) {
            searchView.displayNoInternetWarning()
        } else {
            if (searchQuery.isEmpty()) {
                searchView.displayEmptySearchQueryWarning()
            } else {
                searchView.activateProgressBar()
                repositoryService.getRepositoriesByQualifiersAndKeywords(searchQuery, this)
            }
        }
    }

    override fun navigateToDetailActivity(repositoryName: String, repositoryOwnerName: String) {
        searchView.displayRepositoryDetail(repositoryName, repositoryOwnerName)
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