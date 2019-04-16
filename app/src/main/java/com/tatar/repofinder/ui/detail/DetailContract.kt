package com.tatar.repofinder.ui.detail

import com.tatar.repofinder.ui.base.BaseContract.BasePresenter
import com.tatar.repofinder.ui.base.BaseContract.BaseView

interface DetailContract {

    interface DetailView : BaseView {
        fun displayRepositoryDetails()
    }

    interface DetailPresenter : BasePresenter<DetailView?> {
        fun getRepositoryDetails()
    }
}