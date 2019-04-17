package com.tatar.repofinder.ui.search

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tatar.repofinder.App
import com.tatar.repofinder.R
import com.tatar.repofinder.data.model.Repo
import com.tatar.repofinder.di.search.DaggerSearchComponent
import com.tatar.repofinder.ui.base.BaseActivity
import com.tatar.repofinder.ui.detail.DetailActivity
import com.tatar.repofinder.ui.search.RepoAdapter.ItemClickListener
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject


// TODO need a better practice(RX, LiveData or co-routines) to avoid using runOnUiThread here
class SearchActivity : BaseActivity(), SearchView, ItemClickListener {

    @Inject
    lateinit var repoAdapter: RepoAdapter
    @Inject
    lateinit var searchPresenter: SearchPresenter

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun provideDependencies() {
        val searchComponent = DaggerSearchComponent.builder()
            .searchActivity(this)
            .itemClickListener(this)
            .appComponent(App.instance.appComponent()).build()

        searchComponent.injectSearchActivity(this)
    }

    override fun initViews() {
        repository_recycler_view.layoutManager = LinearLayoutManager(this)
        repository_recycler_view.adapter = repoAdapter

        repository_search_btn.setOnClickListener {
            val searchQuery = repository_search_view.query.toString()
            searchPresenter.performSearch(searchQuery)
        }
    }

    override fun init() {
        searchPresenter.attach(this)
    }

    override fun detachPresenter() {
        searchPresenter.detach()
    }

    override fun showResultContent(repoList: ArrayList<Repo>) {
        runOnUiThread {
            repoAdapter.setRepositoryListItems(repoList)
            search_result_title_tv.visibility = View.VISIBLE
            repository_recycler_view.visibility = View.VISIBLE
        }
    }

    override fun hideResultContent() {
        runOnUiThread {
            search_result_title_tv.visibility = View.GONE
            repository_recycler_view.visibility = View.GONE
        }
    }

    override fun displaySearchingMessage() {
        runOnUiThread { status_tv.text = getString(R.string.status_tv_finding_txt) }
    }

    override fun displayNoRepositoriesFoundMessage() {
        runOnUiThread { setStatusText(getString(R.string.status_tv_not_found_txt)) }
    }

    override fun displayErrorMessage() {
        runOnUiThread { setStatusText(getString(R.string.status_tv_error_txt)) }
    }

    override fun displayEmptyQueryStringToast() {
        displayToastMessage(getString(R.string.empty_search_query_message))
    }

    override fun enableSearchButton() {
        runOnUiThread { repository_search_btn.isEnabled = true }
    }

    override fun disableSearchButton() {
        runOnUiThread { repository_search_btn.isEnabled = false }
    }

    override fun startDetailActivity(repositoryName: String, repositoryOwnerName: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(REPO_NAME_BUNDLE_KEY, repositoryName)
        intent.putExtra(REPO_OWNER_NAME_BUNDLE_KEY, repositoryOwnerName)
        startActivity(intent)
    }

    override fun onItemClick(repo: Repo) {
        searchPresenter.onRepositoryItemClick(repo.name, repo.ownerName)
    }
}
