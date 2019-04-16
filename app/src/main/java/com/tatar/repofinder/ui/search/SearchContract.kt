package com.tatar.repofinder.ui.search

import com.tatar.repofinder.data.model.Repository
import com.tatar.repofinder.ui.base.BaseContract.BasePresenter
import com.tatar.repofinder.ui.base.BaseContract.BaseView

interface SearchContract {

    interface SearchView : BaseView {
        fun displayRepositories(repositoryList: ArrayList<Repository>)
        fun displayNoRepositoriesFoundMessage()
        fun displayEmptyQueryStringToast()
        fun startDetailActivity(repositoryName: String, repositoryOwnerName: String)
    }

    interface SearchPresenter : BasePresenter<SearchView?> {
        fun performSearch(searchQuery: String)
        fun onRepositoryItemClick(repositoryName: String, repositoryOwnerName: String)
    }
}