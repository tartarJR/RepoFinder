package com.tatar.repofinder.ui.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

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
        protected const val REPO_NAME_BUNDLE_KEY = "repo_name"
        protected const val REPO_OWNER_NAME_BUNDLE_KEY = "repo_owner_name"
    }
}