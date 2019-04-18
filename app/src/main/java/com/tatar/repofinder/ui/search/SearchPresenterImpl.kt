package com.tatar.repofinder.ui.search

import com.tatar.repofinder.data.model.Repo
import com.tatar.repofinder.data.service.RepoService
import com.tatar.repofinder.data.service.RepoServiceResponse
import com.tatar.repofinder.data.service.RepoServiceUtil
import com.tatar.repofinder.ui.base.BaseContract.BasePresenter.Companion.DETACHED_VIEW_ERROR
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import com.tatar.repofinder.util.ConnectionManager
import io.reactivex.observers.DisposableObserver
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

// TODO I don't like this if else check for the view
class SearchPresenterImpl(
    private val repoService: RepoService,
    private val connectionManager: ConnectionManager
) : SearchPresenter {

    private val logger = AnkoLogger(SearchPresenterImpl::class.java)

    private var searchView: SearchView? = null
    private var searchResultObserver: DisposableObserver<RepoServiceResponse<Repo>>? = null

    override fun performSearch(searchQuery: String) {
        if (this.searchView != null) {
            if (!connectionManager.hasInternetConnection()) {
                searchView?.hideResultContent()
                searchView?.displayNoInternetWarning()
                searchView?.showStatusTv()
            } else {
                searchView?.hideResultContent()
                searchView?.showProgressBar()
                searchView?.displaySearchingMessage()
                searchView?.showStatusTv()

                searchResultObserver = getSearchResultObserver()
                repoService.getSearchResults(searchQuery.trim()).subscribe(searchResultObserver!!)
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

    override fun attachView(view: SearchView?) {
        this.searchView = view
    }

    override fun detachView() {
        this.searchView = null
    }

    override fun unSubscribeObservable() {
        if (searchResultObserver != null && !searchResultObserver!!.isDisposed) {
            searchResultObserver!!.dispose()
        }
    }

    private fun getSearchResultObserver(): DisposableObserver<RepoServiceResponse<Repo>> {
        return object : DisposableObserver<RepoServiceResponse<Repo>>() {

            override fun onNext(searchResultResponse: RepoServiceResponse<Repo>) {
                if (searchView != null) {
                    searchView?.hideProgressBar()

                    val numberOfReposFound = searchResultResponse.itemCount
                    val repositories = searchResultResponse.items

                    if (repositories.isEmpty()) {
                        searchView?.displayNoRepositoriesFoundMessage()
                    } else {
                        searchView?.hideStatusTv()
                        searchView?.hideKeyboard()

                        if (numberOfReposFound < RepoServiceUtil.NUM_OF_ITEMS_IN_PAGE) {
                            searchView?.displayResultText(numberOfReposFound, numberOfReposFound)
                        } else {
                            searchView?.displayResultText(RepoServiceUtil.NUM_OF_ITEMS_IN_PAGE, numberOfReposFound)
                        }

                        searchView?.displayRepoList(repositories)
                    }
                } else {
                    logger.error(DETACHED_VIEW_ERROR)
                }
            }

            override fun onError(e: Throwable) {

                if (searchView != null) {
                    searchView?.hideResultContent()
                    searchView?.hideProgressBar()
                    searchView?.displayErrorMessage()
                    searchView?.showStatusTv()
                } else {
                    logger.error(DETACHED_VIEW_ERROR)
                }

                logger.error(e)
            }

            override fun onComplete() {
                logger.info("getSearchResults observation completed")
            }
        }
    }
}