package com.tatar.repofinder.ui.base

interface BaseContract {

    interface BasePresenter<in T> {
        fun attach(view: T)
        fun detach()
    }

    interface BaseView {
        fun activateProgressBar()
        fun displayErrorMessage()
        fun displayNoInternetWarning()
    }
}