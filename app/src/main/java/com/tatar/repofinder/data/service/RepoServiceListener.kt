package com.tatar.repofinder.data.service

interface RepoServiceListener<T> {
    fun onResponse(repoServiceResponse: RepoServiceResponse<T>)
    fun onError()
}