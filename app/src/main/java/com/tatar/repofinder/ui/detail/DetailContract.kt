package com.tatar.repofinder.ui.detail

import com.tatar.repofinder.data.model.Subscriber
import com.tatar.repofinder.ui.base.BaseContract.BasePresenter
import com.tatar.repofinder.ui.base.BaseContract.BaseView

interface DetailContract {

    interface DetailView : BaseView {
        fun displayRetrievingDetailsMessage()
        fun displayDetailText(repoName: String, numberOfInitDisplays: Int, numberOfSubscriber: Int)
        fun displayRepositoryDetails(subscribers: ArrayList<Subscriber>)
        fun displayNoSubscriberFoundMessage()
        fun hideSubscriberList()
        fun hideDetailText()
        fun enableSwipeRefresh()
        fun disableSwipeRefresh()
    }

    interface DetailPresenter : BasePresenter<DetailView?> {
        fun getRepositoryDetails(repoName: String, repoOwnerName: String)
    }
}