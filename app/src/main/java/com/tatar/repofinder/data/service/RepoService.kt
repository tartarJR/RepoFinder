package com.tatar.repofinder.data.service

import GetRepositoriesByQualifiersAndKeywordsQuery
import GetRepositoryDetailsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import com.tatar.repofinder.data.model.Repo
import com.tatar.repofinder.data.model.Subscriber
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import type.SearchType

class RepoService(private val apolloClient: ApolloClient) {

    fun getSearchResults(searchParam: String): Observable<RepoServiceResponse<Repo>> {

        val repoSearchQuery = GetRepositoriesByQualifiersAndKeywordsQuery
            .builder()
            .query(searchParam)
            .first(RepoServiceUtil.NUM_OF_ITEMS_IN_PAGE)
            .type(SearchType.REPOSITORY)
            .build()

        val repoSearchCall = apolloClient.query(repoSearchQuery)

        return Rx2Apollo.from(repoSearchCall)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { RepoServiceUtil.convertFromApolloSearchResult(it.data()!!) }
    }

    fun getRepoDetails(
        repositoryName: String,
        repositoryOwnerName: String
    ): Observable<RepoServiceResponse<Subscriber>> {
        val repoDetailQuery = GetRepositoryDetailsQuery
            .builder()
            .name(repositoryName)
            .owner(repositoryOwnerName)
            .first(RepoServiceUtil.NUM_OF_ITEMS_IN_PAGE)
            .build()

        val repoDetailCall = apolloClient.query(repoDetailQuery)

        return Rx2Apollo.from(repoDetailCall)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { RepoServiceUtil.convertFromApolloRepoDetail(it.data()!!) }
    }
}