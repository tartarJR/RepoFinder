package com.tatar.repofinder.ui.detail

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tatar.repofinder.App
import com.tatar.repofinder.R
import com.tatar.repofinder.data.model.Subscriber
import com.tatar.repofinder.di.detail.DaggerDetailComponent
import com.tatar.repofinder.ui.base.BaseActivity
import com.tatar.repofinder.ui.detail.DetailContract.DetailPresenter
import com.tatar.repofinder.ui.detail.DetailContract.DetailView
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject


class DetailActivity : BaseActivity(), DetailView {

    @Inject
    lateinit var subscriberAdapter: SubscriberAdapter
    @Inject
    lateinit var detailPresenter: DetailPresenter

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    override fun provideDependencies() {
        val detailComponent = DaggerDetailComponent.builder()
            .detailActivity(this)
            .appComponent(App.instance.appComponent())
            .build()

        detailComponent.injectDetailActivity(this)
    }

    override fun initViews() {
        subscriber_recycler_view.layoutManager = LinearLayoutManager(this)
        subscriber_recycler_view.adapter = subscriberAdapter

        swipe_refresh_layout.setOnRefreshListener {
            makeDetailCall()
            swipe_refresh_layout.isRefreshing = false
        }
    }

    override fun init() {
        detailPresenter.attachView(this)
        makeDetailCall()
    }

    override fun detachPresenter() {
        detailPresenter.detachView()
    }

    override fun displayRetrievingDetailsMessage() {
        runOnUiThread { setStatusText(getString(R.string.retrieving_details_txt)) }
    }

    override fun displayDetailText(repoName: String, numberOfInitDisplays: Int, numberOfSubscriber: Int) {
        runOnUiThread {
            detail_tv.text = getString(R.string.detail_tv_txt, numberOfInitDisplays, numberOfSubscriber, repoName)
            detail_tv.visibility = View.VISIBLE
        }
    }

    override fun displayRepositoryDetails(subscribers: ArrayList<Subscriber>) {
        runOnUiThread {
            subscriberAdapter.setSubscribers(subscribers)
            subscriber_recycler_view.visibility = View.VISIBLE
        }
    }

    override fun displayNoSubscriberFoundMessage() {
        runOnUiThread { setStatusText(getString(R.string.no_subscribers_found_txt)) }
    }

    override fun hideSubscriberList() {
        subscriber_recycler_view.visibility = View.GONE
    }

    override fun hideDetailText() {
        detail_tv.visibility = View.GONE
    }

    override fun enableSwipeRefresh() {
        swipe_refresh_layout.isEnabled = true
    }

    override fun disableSwipeRefresh() {
        swipe_refresh_layout.isEnabled = false
    }

    override fun displayErrorMessage() {
        runOnUiThread { setStatusText(getString(R.string.detail_error_txt)) }
    }

    override fun displayNoInternetWarning() {
        runOnUiThread { setStatusText(getString(R.string.detail_no_internet_msg)) }
    }

    private fun makeDetailCall() {
        val incomingRepoName = intent.getStringExtra(EXTRA_KEY_REPO_NAME)
        val incomingRepoOwnerName = intent.getStringExtra(EXTRA_KEY_REPO_OWNER_NAME)

        detailPresenter.getRepositoryDetails(incomingRepoName, incomingRepoOwnerName)
    }
}
