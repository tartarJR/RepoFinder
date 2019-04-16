package com.tatar.repofinder.ui.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tatar.repofinder.R

abstract class BaseActivity : AppCompatActivity(), BaseContract.BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
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

    protected fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus

        if (view == null) {
            view = View(this)
        }

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected companion object {
        internal const val REPO_NAME_BUNDLE_KEY = "repo_name"
        internal const val REPO_OWNER_NAME_BUNDLE_KEY = "repo_owner_name"
    }

    override fun displayNoInternetWarning() {
        Toast.makeText(this, getString(R.string.no_internet_connection_message), Toast.LENGTH_SHORT).show()
    }
}