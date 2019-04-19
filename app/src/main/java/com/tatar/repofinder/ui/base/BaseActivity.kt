package com.tatar.repofinder.ui.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tatar.repofinder.R

abstract class BaseActivity : AppCompatActivity(), BaseContract.BaseView {

    private var progressBar: ProgressBar? = null
    private var statusTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        // TODO is it necessary or possible to eliminate findViewById here
        progressBar = findViewById(R.id.progress_bar)
        statusTextView = findViewById(R.id.status_tv)

        provideDependencies()
        initViews()
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePresenterResources()
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun provideDependencies()
    protected abstract fun initViews()
    protected abstract fun init()
    protected abstract fun releasePresenterResources()

    override fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar?.visibility = View.GONE
    }

    override fun showStatusTv() {
        statusTextView?.visibility = View.VISIBLE
    }

    override fun hideStatusTv() {
        statusTextView?.visibility = View.GONE
    }

    override fun displayNoInternetWarning() {
        statusTextView?.text = getString(R.string.no_internet_connection_message)
    }

    override fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus

        if (view == null) {
            view = View(this)
        }

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun setStatusText(message: String) {
        statusTextView?.text = message
    }

    protected companion object {
        internal const val EXTRA_KEY_REPO_NAME = "repo_name"
        internal const val EXTRA_KEY_REPO_OWNER_NAME = "repo_owner_name"
    }
}