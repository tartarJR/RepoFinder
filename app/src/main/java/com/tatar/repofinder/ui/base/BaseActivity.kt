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

    abstract fun getLayoutId(): Int
    abstract fun provideDependencies()
    abstract fun initViews()
    abstract fun init()
    abstract fun detachPresenter()

    protected fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus

        if (view == null) {
            view = View(this)
        }

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        const val REPO_NAME_BUNDLE_KEY = "repo_name"
        const val REPO_OWNER_NAME_BUNDLE_KEY = "repo_owner_name"
    }
}