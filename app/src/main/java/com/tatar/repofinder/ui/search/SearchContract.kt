package com.tatar.repofinder.ui.search

import com.tatar.repofinder.data.model.Repo
import com.tatar.repofinder.ui.base.BaseContract.BasePresenter
import com.tatar.repofinder.ui.base.BaseContract.BaseView

interface SearchContract {

    interface SearchView : BaseView {
        fun displayResultText(numberOfInitDisplays: Int, numberOfReposFound: Int)
        fun displayRepoList(repoList: ArrayList<Repo>)
        fun hideResultContent()
        fun displaySearchingMessage()
        fun displayNoRepositoriesFoundMessage()
        fun startDetailActivity(repositoryName: String, repositoryOwnerName: String)
    }

    interface SearchPresenter : BasePresenter<SearchView?> {
        fun performSearch(searchQuery: String)
        fun onRepositoryItemClick(repositoryName: String, repositoryOwnerName: String)
    }
}