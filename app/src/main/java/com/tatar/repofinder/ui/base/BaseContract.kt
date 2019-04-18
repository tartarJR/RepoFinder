package com.tatar.repofinder.ui.base

interface BaseContract {

    interface BasePresenter<in T> {

        fun attachView(view: T)
        fun detachView()

        companion object {
            const val DETACHED_VIEW_ERROR = "View is detached"
        }
    }

    interface BaseView {
        fun showProgressBar()
        fun hideProgressBar()
        fun showStatusTv()
        fun hideStatusTv()
        fun displayErrorMessage()
        fun displayNoInternetWarning()
        fun hideKeyboard()
    }
}