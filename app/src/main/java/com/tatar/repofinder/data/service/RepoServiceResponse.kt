package com.tatar.repofinder.data.service

data class RepoServiceResponse<T>(
    var itemCount: Int,
    var items: ArrayList<T>
)