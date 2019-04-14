package com.tatar.repofinder.data.service

import GetRepositoriesByQualifiersAndKeywordsQuery
import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.tatar.repofinder.BuildConfig
import com.tatar.repofinder.ui.search.SearchView
import com.tatar.repofinder.data.model.Repository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import org.jetbrains.anko.runOnUiThread
import type.SearchType
import java.util.concurrent.TimeUnit

// TODO experimental class for Apollo and GraphQl, To be refactored
class RepositoryService(var searchView: SearchView, var context: Context) : AnkoLogger {

    fun getRepositoriesByQualifiersAndKeywords(searchParam: String) {

        val queryCall = GetRepositoriesByQualifiersAndKeywordsQuery
            .builder()
            .query(searchParam)
            .first(25)
            .type(SearchType.REPOSITORY)
            .build()

        apolloClient.query(queryCall)
            .enqueue(object : ApolloCall.Callback<GetRepositoriesByQualifiersAndKeywordsQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    searchView.displayErrorMessage()
                    error(e)
                }

                override fun onResponse(response: com.apollographql.apollo.api.Response<GetRepositoriesByQualifiersAndKeywordsQuery.Data>) {
                    val errors = response.errors()
                    if (errors.isNotEmpty()) {
                        searchView.displayErrorMessage()
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

                        context.runOnUiThread {

                            if (repositories.isEmpty()) {
                                searchView.displayNotFoundMessage()
                            } else {
                                searchView.displayRepositories(repositories)
                            }
                        }
                    }
                }
            })
    }

    companion object {

        private const val GITHUB_API_BASE_URL = "https://api.github.com/graphql"

        private val httpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(NetworkInterceptor())
                .build()
        }

        private val apolloClient: ApolloClient by lazy {
            ApolloClient.builder()
                .serverUrl(GITHUB_API_BASE_URL)
                .okHttpClient(httpClient)
                .build()
        }

        private class NetworkInterceptor : Interceptor {

            override fun intercept(chain: Interceptor.Chain?): Response {
                return chain!!.proceed(
                    chain.request().newBuilder().header(
                        "Authorization",
                        "Bearer ${BuildConfig.GITHUB_AUTH_TOKEN}"
                    ).build()
                )
            }
        }

    }

}