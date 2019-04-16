package com.tatar.repofinder.ui.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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
        detachPresenter()
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun provideDependencies()
    protected abstract fun initViews()
    protected abstract fun init()
    protected abstract fun detachPresenter()

    override fun showProgressBar() {
        runOnUiThread { progressBar?.visibility = View.VISIBLE }
    }

    override fun hideProgressBar() {
        runOnUiThread { progressBar?.visibility = View.GONE }
    }

    override fun showStatusTv() {
        runOnUiThread { statusTextView?.visibility = View.VISIBLE }
    }

    override fun hideStatusTv() {
        runOnUiThread { statusTextView?.visibility = View.GONE }
    }

    override fun displayNoInternetWarning() {
        Toast.makeText(this, getString(R.string.no_internet_connection_message), Toast.LENGTH_SHORT).show()
    }

    override fun hideKeyboard() {
        runOnUiThread {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = currentFocus

            if (view == null) {
                view = View(this)
            }

            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    protected fun setStatusText(message: String) {
        runOnUiThread { statusTextView?.text = message }
    }

    protected fun displayToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected companion object {
        internal const val REPO_NAME_BUNDLE_KEY = "repo_name"
        internal const val REPO_OWNER_NAME_BUNDLE_KEY = "repo_owner_name"
    }
}