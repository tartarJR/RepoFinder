package com.tatar.repofinder.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tatar.repofinder.App
import com.tatar.repofinder.R
import com.tatar.repofinder.data.model.Repository
import com.tatar.repofinder.di.search.DaggerSearchComponent
import com.tatar.repofinder.ui.detail.DetailActivity
import com.tatar.repofinder.ui.search.RepositoryAdapter.ItemClickListener
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), SearchView, ItemClickListener {

    @Inject
    lateinit var repositoryAdapter: RepositoryAdapter
    @Inject
    lateinit var searchPresenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        provideDependencies()
        setUpRecyclerView()
        searchPresenter.attach(this)

        repository_search_btn.setOnClickListener {
            val searchQuery = repository_search_view.query.toString()
            searchPresenter.performSearch(searchQuery)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.detach()
    }

    override fun displayRepositories(repositoryList: ArrayList<Repository>) {
        // TODO need a better practice(RX or co-routines), avoid runOnUiThread
        runOnUiThread {
            repositoryAdapter.setRepositoryListItems(repositoryList)
            progress_bar.visibility = View.GONE
            status_tv.visibility = View.GONE
            search_result_title_tv.visibility = View.VISIBLE
            repository_recycler_view.visibility = View.VISIBLE
            repository_search_btn.isEnabled = true
        }
    }

    override fun activateProgressBar() {
        if (repository_recycler_view.visibility == View.VISIBLE) {
            repository_recycler_view.visibility = View.GONE
            search_result_title_tv.visibility = View.GONE
        }

        progress_bar.visibility = View.VISIBLE
        status_tv.text = getString(R.string.status_tv_finding_txt)
        repository_search_btn.isEnabled = false
    }

    override fun displayErrorMessage() {
        // TODO need a better practice(RX or co-routines), avoid runOnUiThread
        runOnUiThread {
            displayMessage(getString(R.string.status_tv_error_txt))
        }
    }

    override fun displayNoRepositoriesFoundMessage() {
        // TODO need a better practice(RX or co-routines), avoid runOnUiThread
        runOnUiThread {
            displayMessage(getString(R.string.status_tv_not_found_txt))
        }
    }

    override fun displayEmptyQueryStringToast() {
        Toast.makeText(this, getString(R.string.empty_search_query_message), Toast.LENGTH_SHORT).show()
    }

    override fun displayNoInternetWarning() {
        Toast.makeText(this, getString(R.string.no_internet_connection_message), Toast.LENGTH_SHORT).show()
    }

    override fun startDetailActivity(repositoryName: String, repositoryOwnerName: String) {
        var intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("repo_name", repositoryName)
        intent.putExtra("repo_owner_name", repositoryOwnerName)
        startActivity(intent)
    }

    override fun onItemClick(repository: Repository) {
        searchPresenter.onRepositoryItemClick(repository.name, repository.ownerName)
    }

    private fun displayMessage(message: String) {
        status_tv.text = message
        status_tv.visibility = View.VISIBLE
        progress_bar.visibility = View.INVISIBLE
        repository_search_view.visibility = View.VISIBLE
        repository_search_btn.visibility = View.VISIBLE
        repository_search_btn.isEnabled = true
    }

    private fun provideDependencies() {
        val searchComponent = DaggerSearchComponent.builder()
            .searchActivity(this)
            .itemClickListener(this)
            .appComponent(App.instance.appComponent()).build()

        searchComponent.injectSearchActivity(this)
    }

    private fun setUpRecyclerView() {
        repository_recycler_view.layoutManager = LinearLayoutManager(this)
        repository_recycler_view.adapter = repositoryAdapter
    }
}
