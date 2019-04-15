package com.tatar.repofinder.ui.search

import com.tatar.repofinder.data.model.Repository

interface SearchContract {

    interface SearchView {
        fun displayRepositories(repositoryList: ArrayList<Repository>)
        fun activateProgressBar()
        fun displayErrorMessage()
        fun displayNotFoundMessage()
        fun displayEmptySearchQueryWarning()
        fun displayNoInternetWarning()
    }

    interface SearchPresenter {
        fun performSearch(searchQuery: String)
    }
}