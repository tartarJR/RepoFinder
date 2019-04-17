package com.tatar.repofinder.ui.search

import android.content.Intent
import android.view.Menu
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.miguelcatalan.materialsearchview.MaterialSearchView
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(menuItem)
        return true
    }

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
        setSupportActionBar(tool_bar)

        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchPresenter.performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        repository_recycler_view.layoutManager = LinearLayoutManager(this)
        repository_recycler_view.adapter = repoAdapter
    }

    override fun init() {
        searchPresenter.attach(this)
    }

    override fun detachPresenter() {
        searchPresenter.detach()
    }

    override fun displayRepoList(repoList: ArrayList<Repo>) {
        runOnUiThread {
            repoAdapter.setRepos(repoList)
            repository_recycler_view.visibility = View.VISIBLE
        }
    }

    override fun displayResultText(numberOfInitDisplays: Int, numberOfReposFound: Int) {
        runOnUiThread {
            search_result_title_tv.text =
                getString(R.string.search_result_title_tv_txt, numberOfInitDisplays, numberOfReposFound)
            search_result_title_tv.visibility = View.VISIBLE
        }
    }

    override fun hideResultContent() {
        runOnUiThread {
            search_result_title_tv.visibility = View.GONE
            repository_recycler_view.visibility = View.GONE
        }
    }

    override fun displaySearchingMessage() {
        runOnUiThread { status_tv.text = getString(R.string.searching_repos_txt) }
    }

    override fun displayNoRepositoriesFoundMessage() {
        runOnUiThread { setStatusText(getString(R.string.no_repos_found_txt)) }
    }

    override fun displayErrorMessage() {
        runOnUiThread { setStatusText(getString(R.string.search_error_txt)) }
    }

    override fun displayEmptyQueryStringToast() {
        displayToastMessage(getString(R.string.empty_search_query_message))
    }

    override fun startDetailActivity(repositoryName: String, repositoryOwnerName: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(EXTRA_KEY_REPO_NAME, repositoryName)
        intent.putExtra(EXTRA_KEY_REPO_OWNER_NAME, repositoryOwnerName)
        startActivity(intent)
    }

    override fun onItemClick(repo: Repo) {
        searchPresenter.onRepositoryItemClick(repo.name, repo.ownerName)
    }
}
