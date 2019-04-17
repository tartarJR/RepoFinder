package com.tatar.repofinder.ui.search

import com.tatar.repofinder.data.model.Repo
import com.tatar.repofinder.ui.base.BaseContract.BasePresenter
import com.tatar.repofinder.ui.base.BaseContract.BaseView

interface SearchContract {

    interface SearchView : BaseView {
        fun showResultContent(repoList: ArrayList<Repo>)
        fun hideResultContent()
        fun displaySearchingMessage()
        fun displayNoRepositoriesFoundMessage()
        fun displayEmptyQueryStringToast()
        fun enableSearchButton()
        fun disableSearchButton()
        fun startDetailActivity(repositoryName: String, repositoryOwnerName: String)
    }

    interface SearchPresenter : BasePresenter<SearchView?> {
        fun performSearch(searchQuery: String)
        fun onRepositoryItemClick(repositoryName: String, repositoryOwnerName: String)
    }
}