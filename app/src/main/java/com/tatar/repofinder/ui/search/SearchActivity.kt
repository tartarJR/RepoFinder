package com.tatar.repofinder.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tatar.repofinder.R
import com.tatar.repofinder.data.model.Repository
import com.tatar.repofinder.data.service.RepositoryService
import com.tatar.repofinder.ui.search.SearchContract.SearchView
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.AnkoLogger

class SearchActivity : AppCompatActivity(), SearchView, AnkoLogger {

    private lateinit var repositoryAdapter: RepositoryAdapter
    private lateinit var repositoryService: RepositoryService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        repositoryService = RepositoryService(this, this)

        repositoryAdapter = RepositoryAdapter {
            Toast.makeText(this, "${it.name} Clicked", Toast.LENGTH_SHORT).show()
        }

        repository_recycler_view.layoutManager = LinearLayoutManager(this) // TODO move this to XML
        repository_recycler_view.adapter = repositoryAdapter

        repository_search_btn.setOnClickListener {
            val searchQuery = repository_search_view.query.toString()

            if (searchQuery.isEmpty()) {

            } else {
                activateProgressBar()
                repositoryService.getRepositoriesByQualifiersAndKeywords(searchQuery) // TODO move validation to presenter
            }
        }
    }

    override fun displayRepositories(repositoryList: ArrayList<Repository>) {
        repositoryAdapter.setRepositoryListItems(repositoryList)
        progress_bar.visibility = View.GONE
        status_tv.visibility = View.GONE
        search_result_title_tv.visibility = View.VISIBLE
        repository_recycler_view.visibility = View.VISIBLE
        repository_search_btn.isEnabled = true
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
        displayMessage(getString(R.string.status_tv_error_txt))
    }

    override fun displayNotFoundMessage() {
        displayMessage(getString(R.string.status_tv_not_found_txt))
    }

    override fun displayEmptySearchQueryWarning() {
        Toast.makeText(this, getString(R.string.empty_search_query_message), Toast.LENGTH_SHORT).show()
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
