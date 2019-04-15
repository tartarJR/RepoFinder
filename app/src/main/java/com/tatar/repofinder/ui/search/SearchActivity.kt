package com.tatar.repofinder.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tatar.repofinder.App
import com.tatar.repofinder.R
import com.tatar.repofinder.data.model.Repository
import com.tatar.repofinder.di.search.DaggerSearchComponent
import com.tatar.repofinder.di.search.SearchModule
import com.tatar.repofinder.ui.search.RepositoryAdapter.ItemClickListener
import com.tatar.repofinder.ui.search.SearchContract.SearchPresenter
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), SearchView, ItemClickListener, AnkoLogger {

    @Inject
    lateinit var repositoryAdapter: RepositoryAdapter
    @Inject
    lateinit var searchPresenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        var searchComponent = DaggerSearchComponent.builder()
            .searchModule(SearchModule(this, this, this))
            .appComponent(App.instance.appComponent())
            .build()

        searchComponent.injectSearchActivity(this)

        repository_recycler_view.layoutManager = LinearLayoutManager(this)
        repository_recycler_view.adapter = repositoryAdapter

        repository_search_btn.setOnClickListener {
            val searchQuery = repository_search_view.query.toString()
            searchPresenter.performSearch(searchQuery)
        }
    }

    override fun displayRepositories(repositoryList: ArrayList<Repository>) {
        runOnUiThread {
            // TODO need a better practice
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
        runOnUiThread {
            // TODO need a better practice
            displayMessage(getString(R.string.status_tv_error_txt))
        }
    }

    override fun displayNotFoundMessage() {
        runOnUiThread {
            // TODO need a better practice
            displayMessage(getString(R.string.status_tv_not_found_txt))
        }
    }

    override fun displayEmptySearchQueryWarning() {
        Toast.makeText(this, getString(R.string.empty_search_query_message), Toast.LENGTH_SHORT).show()
    }

    override fun displayNoInternetWarning() {
        Toast.makeText(this, getString(R.string.no_internet_connection_message), Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(repository: Repository) {
        Toast.makeText(this, "${repository.name} Clicked", Toast.LENGTH_SHORT).show() // TODO implement this
    }

    private fun displayMessage(message: String) {
        status_tv.text = message
        status_tv.visibility = View.VISIBLE
        progress_bar.visibility = View.INVISIBLE
        repository_search_view.visibility = View.VISIBLE
        repository_search_btn.visibility = View.VISIBLE
        repository_search_btn.isEnabled = true
    }
}
