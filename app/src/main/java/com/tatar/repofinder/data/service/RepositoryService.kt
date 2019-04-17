package com.tatar.repofinder.data.service

import GetRepositoriesByQualifiersAndKeywordsQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.tatar.repofinder.data.model.Repository
import com.tatar.repofinder.data.model.Subscriber
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import type.SearchType

class RepositoryService(private val apolloClient: ApolloClient) : AnkoLogger {

    fun getRepositoriesByQualifiersAndKeywords(searchParam: String, responseListener: ResponseListener<Repository>) {

        val searchQuery = GetRepositoriesByQualifiersAndKeywordsQuery
            .builder()
            .query(searchParam)
            .first(NUM_OF_ITEMS_IN_PAGE)
            .type(SearchType.REPOSITORY)
            .build()

        apolloClient.query(searchQuery)
            .enqueue(object : ApolloCall.Callback<GetRepositoriesByQualifiersAndKeywordsQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    responseListener.onError()
                    error(e)
                }

                override fun onResponse(response: com.apollographql.apollo.api.Response<GetRepositoriesByQualifiersAndKeywordsQuery.Data>) {

                    val errors = response.errors()

                    if (errors.isNotEmpty()) {
                        responseListener.onError()
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

                        responseListener.onResponse(repositories)
                    }
                }
            })
    }

    fun getRepositoryDetails(
        repositoryName: String,
        repositoryOwnerName: String,
        responseListener: ResponseListener<Subscriber>
    ) {

        val searchQuery = GetRepositoryDetailsQuery
            .builder()
            .name(repositoryName)
            .owner(repositoryOwnerName)
            .first(NUM_OF_ITEMS_IN_PAGE)
            .build()

        apolloClient.query(searchQuery)
            .enqueue(object : ApolloCall.Callback<GetRepositoryDetailsQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    responseListener.onError()
                    error(e)
                }

                override fun onResponse(response: com.apollographql.apollo.api.Response<GetRepositoryDetailsQuery.Data>) {

                    val errors = response.errors()

                    if (errors.isNotEmpty()) {
                        responseListener.onError()
                        val message = errors[0]?.message() ?: ""
                        error("ERROR: $message")
                    } else {
                        val apolloRepositoryDetail = response.data()?.repository()
                        val subscriberEdges = apolloRepositoryDetail?.watchers()?.edges()

                        val subscribers = arrayListOf<Subscriber>()

                        if (subscriberEdges != null) {
                            for (edge in subscriberEdges) {
                                val apolloSubscriber = edge.node()

                                val subscriber = Subscriber(
                                    apolloSubscriber!!.login(),
                                    apolloSubscriber.avatarUrl().toString(),
                                    apolloSubscriber.bio()
                                )

                                subscribers.add(subscriber)
                            }
                        }

                        responseListener.onResponse(subscribers)
                    }
                }
            })
    }

    interface ResponseListener<T> {
        fun onResponse(responseItems: ArrayList<T>)
        fun onError()
    }

    companion object {
        private const val NUM_OF_ITEMS_IN_PAGE = 25
    }


}