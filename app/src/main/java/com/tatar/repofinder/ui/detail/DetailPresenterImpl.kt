package com.tatar.repofinder.ui.detail

import com.tatar.repofinder.data.model.Subscriber
import com.tatar.repofinder.data.service.RepoService
import com.tatar.repofinder.data.service.RepoServiceListener
import com.tatar.repofinder.data.service.RepoServiceResponse
import com.tatar.repofinder.ui.base.BaseContract
import com.tatar.repofinder.ui.detail.DetailContract.DetailPresenter
import com.tatar.repofinder.ui.detail.DetailContract.DetailView
import com.tatar.repofinder.util.ConnectionManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class DetailPresenterImpl(
    private val repoService: RepoService,
    private val connectionManager: ConnectionManager
) : DetailPresenter, RepoServiceListener<Subscriber> {

    private val logger = AnkoLogger(DetailPresenterImpl::class.java)

    private var detailView: DetailView? = null

    private var incomingRepoName = ""

    override fun getRepositoryDetails(repoName: String, repoOwnerName: String) {

        this.incomingRepoName = repoName

        if (this.detailView != null) {
            if (!connectionManager.hasInternetConnection()) {
                detailView?.displayNoInternetWarning()
                detailView?.hideProgressBar()
                detailView?.enableSwipeRefresh()
            } else {
                detailView?.disableSwipeRefresh()
                detailView?.showProgressBar()
                detailView?.displayRetrievingDetailsMessage()
                repoService.getRepositoryDetails(repoName, repoOwnerName, this)
            }
        } else {
            logger.error(BaseContract.BasePresenter.DETACHED_VIEW_ERROR)
        }
    }

    override fun attach(view: DetailView?) {
        this.detailView = view
    }

    override fun detach() {
        this.detailView = null
    }

    override fun onResponse(repoServiceResponse: RepoServiceResponse<Subscriber>) {
        if (this.detailView != null) {
            detailView?.hideProgressBar()

            val numberOfSubscribers = repoServiceResponse.itemCount
            val subscribers = repoServiceResponse.items

            if (numberOfSubscribers < RepoService.NUM_OF_ITEMS_IN_PAGE) {
                detailView?.displayDetailText(incomingRepoName, numberOfSubscribers, numberOfSubscribers)
            } else {
                detailView?.displayDetailText(incomingRepoName, RepoService.NUM_OF_ITEMS_IN_PAGE, numberOfSubscribers)
            }

            if (subscribers.isEmpty()) {
                detailView?.displayNoSubscriberFoundMessage()
            } else {
                detailView?.hideStatusTv()
                detailView?.displayRepositoryDetails(subscribers)
            }
        } else {
            logger.error(BaseContract.BasePresenter.DETACHED_VIEW_ERROR)
        }
    }

    override fun onError() {
        if (this.detailView != null) {
            detailView?.hideDetailText()
            detailView?.hideSubscriberList()
            detailView?.hideProgressBar()
            detailView?.displayErrorMessage()
            detailView?.showStatusTv()
        } else {
            logger.error(BaseContract.BasePresenter.DETACHED_VIEW_ERROR)
        }
    }
}