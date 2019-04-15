package com.tatar.repofinder.data.service

import GetRepositoriesByQualifiersAndKeywordsQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.exception.ApolloException
import com.tatar.repofinder.App
import com.tatar.repofinder.data.model.Repository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import type.SearchType

class RepositoryService : AnkoLogger {

    private var apolloClient = App.instance.appComponent().apolloClient()

    fun getRepositoriesByQualifiersAndKeywords(searchParam: String, onFinishedListener: OnFinishedListener) {

        val queryCall = GetRepositoriesByQualifiersAndKeywordsQuery
            .builder()
            .query(searchParam)
            .first(25)
            .type(SearchType.REPOSITORY)
            .build()

        apolloClient.query(queryCall)
            .enqueue(object : ApolloCall.Callback<GetRepositoriesByQualifiersAndKeywordsQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    onFinishedListener.onError()
                    error(e)
                }

                override fun onResponse(response: com.apollographql.apollo.api.Response<GetRepositoriesByQualifiersAndKeywordsQuery.Data>) {

                    val errors = response.errors()

                    if (errors.isNotEmpty()) {
                        onFinishedListener.onError()
                        val message = errors[0]?.message() ?: ""
                        error("ERROR: $message")
                    } else {
                        val edges = response.data()!!.search().edges()

                        val repositories = arrayListOf<Repository>()

                        for (edge in edges!!) {
                            val apolloRepository = edge.node()!!.asRepository()

                            // TODO better way to  do this, ?,!! check
                            val repository = Repository(
                                apolloRepository!!.name(),
                                apolloRepository.description(), // TODO fix this
                                apolloRepository.forkCount(),
                                apolloRepository.owner().login(),
                                apolloRepository.owner().avatarUrl().toString(),
                                apolloRepository.primaryLanguage()?.name()
                            )

                            repositories.add(repository)
                        }

                        onFinishedListener.onResponse(repositories)
                    }
                }
            })
    }

    interface OnFinishedListener {
        fun onResponse(repositoryList: ArrayList<Repository>)
        fun onError()
    }
}