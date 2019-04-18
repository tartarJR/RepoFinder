package com.tatar.repofinder.ui.detail

import com.tatar.repofinder.data.model.Subscriber
import com.tatar.repofinder.data.service.RepoService
import com.tatar.repofinder.data.service.RepoServiceResponse
import com.tatar.repofinder.data.service.RepoServiceUtil
import com.tatar.repofinder.ui.base.BaseContract.BasePresenter.Companion.DETACHED_VIEW_ERROR
import com.tatar.repofinder.ui.detail.DetailContract.DetailPresenter
import com.tatar.repofinder.ui.detail.DetailContract.DetailView
import com.tatar.repofinder.util.ConnectionManager
import io.reactivex.observers.DisposableObserver
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class DetailPresenterImpl(
    private val repoService: RepoService,
    private val connectionManager: ConnectionManager
) : DetailPresenter {

    private val logger = AnkoLogger(DetailPresenterImpl::class.java)

    private var incomingRepoName = ""
    private var detailView: DetailView? = null
    private var repoDetailObserver: DisposableObserver<RepoServiceResponse<Subscriber>>? = null

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

                repoDetailObserver = getRepoDetailObserver()
                repoService.getRepoDetails(repoName, repoOwnerName).subscribe(repoDetailObserver!!)
            }
        } else {
            logger.error(DETACHED_VIEW_ERROR)
        }
    }

    override fun attachView(view: DetailView?) {
        this.detailView = view
    }

    override fun detachView() {
        this.detailView = null
    }

    override fun unSubscribeObservable() {
        if (repoDetailObserver != null && !repoDetailObserver!!.isDisposed) {
            repoDetailObserver!!.dispose()
        }
    }

    private fun getRepoDetailObserver(): DisposableObserver<RepoServiceResponse<Subscriber>> {

        return object : DisposableObserver<RepoServiceResponse<Subscriber>>() {

            override fun onNext(repoDetailResponse: RepoServiceResponse<Subscriber>) {
                if (detailView != null) {
                    detailView?.hideProgressBar()

                    val numberOfSubscribers = repoDetailResponse.itemCount
                    val subscribers = repoDetailResponse.items

                    if (numberOfSubscribers < RepoServiceUtil.NUM_OF_ITEMS_IN_PAGE) {
                        detailView?.displayDetailText(
                            incomingRepoName,
                            numberOfSubscribers,
                            numberOfSubscribers
                        )
                    } else {
                        detailView?.displayDetailText(
                            incomingRepoName,
                            RepoServiceUtil.NUM_OF_ITEMS_IN_PAGE,
                            numberOfSubscribers
                        )
                    }

                    if (subscribers.isEmpty()) {
                        detailView?.displayNoSubscriberFoundMessage()
                    } else {
                        detailView?.hideStatusTv()
                        detailView?.displayRepositoryDetails(subscribers)
                    }
                } else {
                    logger.error(DETACHED_VIEW_ERROR)
                }
            }

            override fun onError(e: Throwable) {

                logger.error(e)

                if (detailView != null) {
                    detailView?.hideDetailText()
                    detailView?.hideSubscriberList()
                    detailView?.hideProgressBar()
                    detailView?.displayErrorMessage()
                    detailView?.showStatusTv()
                } else {
                    logger.error(DETACHED_VIEW_ERROR)
                }
            }

            override fun onComplete() {
                logger.info("getRepoDetails observation completed")
            }
        }
    }
}