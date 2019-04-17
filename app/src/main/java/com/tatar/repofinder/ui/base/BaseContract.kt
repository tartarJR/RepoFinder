package com.tatar.repofinder.ui.base

interface BaseContract {

    interface BasePresenter<in T> {

        fun attach(view: T)
        fun detach()

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