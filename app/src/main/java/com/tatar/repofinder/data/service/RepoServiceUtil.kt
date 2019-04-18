package com.tatar.repofinder.data.service

import GetRepositoriesByQualifiersAndKeywordsQuery
import GetRepositoryDetailsQuery
import com.tatar.repofinder.data.model.Repo
import com.tatar.repofinder.data.model.Subscriber

object RepoServiceUtil {

    internal const val NUM_OF_ITEMS_IN_PAGE = 25

    internal fun convertFromApolloSearchResult(apolloSearchResult: GetRepositoriesByQualifiersAndKeywordsQuery.Data): RepoServiceResponse<Repo> {
        val repoResult = apolloSearchResult.search()
        val repoEdges = repoResult.edges()!!
        val repositoryCount = repoResult.repositoryCount()

        val repositories = arrayListOf<Repo>()

        for (edge in repoEdges) {
            val apolloRepository = edge.node()!!.asRepository()!!

            val repository = Repo(
                apolloRepository.name(),
                apolloRepository.description(),
                apolloRepository.forkCount(),
                apolloRepository.owner().login(),
                apolloRepository.owner().avatarUrl().toString(),
                apolloRepository.primaryLanguage()?.name()
            )

            repositories.add(repository)
        }

        return RepoServiceResponse(repositoryCount, repositories)
    }

    @JvmStatic
    internal fun convertFromApolloRepoDetail(apolloRepoDetail: GetRepositoryDetailsQuery.Data): RepoServiceResponse<Subscriber> {
        val repoDetailResult = apolloRepoDetail.repository()!!
        val subscriberEdges = repoDetailResult.watchers().edges()!!
        val subscriberCount = repoDetailResult.watchers().totalCount()

        val subscribers = arrayListOf<Subscriber>()

        for (edge in subscriberEdges) {
            val apolloSubscriber = edge.node()!!

            val subscriber = Subscriber(
                apolloSubscriber.login(),
                apolloSubscriber.avatarUrl().toString(),
                apolloSubscriber.bio()
            )

            subscribers.add(subscriber)
        }

        return RepoServiceResponse(subscriberCount, subscribers)
    }
}