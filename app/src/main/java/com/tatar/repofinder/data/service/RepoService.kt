package com.tatar.repofinder.data.service

import GetRepositoriesByQualifiersAndKeywordsQuery
import GetRepositoryDetailsQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.tatar.repofinder.data.model.Repo
import com.tatar.repofinder.data.model.Subscriber
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import type.SearchType

class RepoService(private val apolloClient: ApolloClient) {

    private val logger = AnkoLogger(ApolloClient::class.java)

    fun getRepositoriesByQualifiersAndKeywords(searchParam: String, repoServiceListener: RepoServiceListener<Repo>) {

        val searchQuery = GetRepositoriesByQualifiersAndKeywordsQuery
            .builder()
            .query(searchParam + DEFAULT_SORT_PARAM)
            .first(NUM_OF_ITEMS_IN_PAGE)
            .type(SearchType.REPOSITORY)
            .build()

        apolloClient.query(searchQuery)
            .enqueue(object : ApolloCall.Callback<GetRepositoriesByQualifiersAndKeywordsQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    repoServiceListener.onError()
                    error(e)
                }

                override fun onResponse(response: Response<GetRepositoriesByQualifiersAndKeywordsQuery.Data>) {

                    val errors = response.errors()

                    if (errors.isNotEmpty()) {
                        repoServiceListener.onError()
                        for (error in response.errors()) logger.error("ERROR: ${error.message()}")
                    } else {
                        val repositoryEdges = response.data()!!.search().edges()
                        val repositoryCount = response.data()!!.search().repositoryCount()

                        val repositories = arrayListOf<Repo>()

                        for (edge in repositoryEdges!!) {
                            val apolloRepository = edge.node()!!.asRepository()

                            val repository = Repo(
                                apolloRepository!!.name(),
                                apolloRepository.description(),
                                apolloRepository.forkCount(),
                                apolloRepository.owner().login(),
                                apolloRepository.owner().avatarUrl().toString(),
                                apolloRepository.primaryLanguage()?.name()
                            )

                            repositories.add(repository)
                        }

                        repoServiceListener.onResponse(RepoServiceResponse(repositoryCount, repositories))
                    }
                }
            })
    }

    fun getRepositoryDetails(
        repositoryName: String,
        repositoryOwnerName: String,
        repoServiceListener: RepoServiceListener<Subscriber>
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
                    repoServiceListener.onError()
                    logger.error(e)
                }

                override fun onResponse(response: Response<GetRepositoryDetailsQuery.Data>) {

                    if (response.errors().isNotEmpty()) {
                        repoServiceListener.onError()
                        for (error in response.errors()) logger.error("ERROR: ${error.message()}")
                    } else {
                        val apolloRepositoryDetail = response.data()?.repository()
                        val subscriberEdges = apolloRepositoryDetail?.watchers()?.edges()
                        val subscriberCount = apolloRepositoryDetail?.watchers()?.totalCount()

                        val subscribers = arrayListOf<Subscriber>()

                        for (edge in subscriberEdges!!) {
                            val apolloSubscriber = edge.node()

                            val subscriber = Subscriber(
                                apolloSubscriber!!.login(),
                                apolloSubscriber.avatarUrl().toString(),
                                apolloSubscriber.bio()
                            )

                            subscribers.add(subscriber)
                        }

                        repoServiceListener.onResponse(RepoServiceResponse(subscriberCount!!, subscribers))
                    }
                }
            })
    }

    companion object {
        private const val NUM_OF_ITEMS_IN_PAGE = 25
        private const val DEFAULT_SORT_PARAM = " sort:stars-desc"
    }
}