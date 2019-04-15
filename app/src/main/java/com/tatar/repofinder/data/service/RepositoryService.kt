package com.tatar.repofinder.data.service

import GetRepositoriesByQualifiersAndKeywordsQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.tatar.repofinder.data.model.Repository
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import type.SearchType

class RepositoryService(private val apolloClient: ApolloClient) : AnkoLogger {

    fun getRepositoriesByQualifiersAndKeywords(searchParam: String, onFinishedListener: OnFinishedListener) {

        val searchQuery = GetRepositoriesByQualifiersAndKeywordsQuery
            .builder()
            .query(searchParam)
            .first(25)
            .type(SearchType.REPOSITORY)
            .build()

        apolloClient.query(searchQuery)
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

                            val repository = Repository(
                                apolloRepository!!.name(),
                                (if (apolloRepository.description() == null) "--" else apolloRepository.description())!!,
                                apolloRepository.forkCount(),
                                apolloRepository.owner().login(),
                                apolloRepository.owner().avatarUrl().toString(),
                                if (apolloRepository.primaryLanguage() == null) "--" else apolloRepository.primaryLanguage()!!.name()
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