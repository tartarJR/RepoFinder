package com.tatar.repofinder

import android.util.Log
import com.tatar.repofinder.data.model.Subscriber
import com.tatar.repofinder.data.service.RepoService
import com.tatar.repofinder.data.service.RepoServiceResponse
import com.tatar.repofinder.ui.detail.DetailContract.DetailPresenter
import com.tatar.repofinder.ui.detail.DetailContract.DetailView
import com.tatar.repofinder.ui.detail.DetailPresenterImpl
import com.tatar.repofinder.util.ConnectionManager
import com.tatar.repofinder.util.RxImmediateSchedulerRule
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner


@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class)
class DetailPresenterTest {

    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var mockDetailView: DetailView

    @Mock
    lateinit var mockRepoService: RepoService

    @Mock
    lateinit var mockConnectionManger: ConnectionManager

    lateinit var detailPresenter: DetailPresenter

    @Before
    fun setUp() {
        PowerMockito.mockStatic(Log::class.java)
        MockitoAnnotations.initMocks(this)
        detailPresenter = DetailPresenterImpl(mockRepoService, mockConnectionManger)
    }

    @Test
    fun test_getRepoDetails_foundCase() {

        val fakeSubscriber = Subscriber(FAKE_REPO_NAME, FAKE_REPO_OWNER_AVATAR_URL, FAKE_REPO_OWNER_BIO)
        val fakeSubscribers = arrayListOf(fakeSubscriber)

        val repoDetailResponse: RepoServiceResponse<Subscriber> = RepoServiceResponse(1, fakeSubscribers)

        val repoDetailObservable =
            Observable.create<RepoServiceResponse<Subscriber>> { it.onNext(repoDetailResponse) }

        `when`(mockRepoService.getRepoDetails(FAKE_REPO_NAME, FAKE_REPO_OWNER_NAME)).thenReturn(repoDetailObservable)
        `when`(mockConnectionManger.hasInternetConnection()).thenReturn(true)

        detailPresenter.attachView(mockDetailView)
        detailPresenter.getRepositoryDetails(FAKE_REPO_NAME, FAKE_REPO_OWNER_NAME)

        verify(mockDetailView).hideProgressBar()
        verify(mockDetailView).displayDetailText(FAKE_REPO_NAME, 1, 1)
        verify(mockDetailView).hideStatusTv()
        verify(mockDetailView).displayRepositoryDetails(fakeSubscribers)
    }

    @Test
    fun test_getRepoDetails_notFoundCase() {

        val noRepoDetailResponse: RepoServiceResponse<Subscriber> = RepoServiceResponse(0, arrayListOf())

        val noRepoDetailObservable =
            Observable.create<RepoServiceResponse<Subscriber>> { it.onNext(noRepoDetailResponse) }

        `when`(mockRepoService.getRepoDetails(FAKE_REPO_NAME, FAKE_REPO_OWNER_NAME)).thenReturn(noRepoDetailObservable)
        `when`(mockConnectionManger.hasInternetConnection()).thenReturn(true)

        detailPresenter.attachView(mockDetailView)
        detailPresenter.getRepositoryDetails(FAKE_REPO_NAME, FAKE_REPO_OWNER_NAME)

        verify(mockDetailView).hideProgressBar()
        verify(mockDetailView).displayDetailText(FAKE_REPO_NAME, 0, 0)
        verify(mockDetailView).displayNoSubscriberFoundMessage()
    }

    @Test
    fun test_getRepoDetails_notInternetCase() {

        `when`(mockConnectionManger.hasInternetConnection()).thenReturn(false)

        detailPresenter.attachView(mockDetailView)
        detailPresenter.getRepositoryDetails(FAKE_REPO_NAME, FAKE_REPO_OWNER_NAME)

        verify(mockDetailView).displayNoInternetWarning()
        verify(mockDetailView).hideProgressBar()
        verify(mockDetailView).enableSwipeRefresh()
    }

    @Test
    fun test_getRepoDetails_errorCase() {

        `when`(mockRepoService.getRepoDetails(FAKE_REPO_NAME, FAKE_REPO_OWNER_NAME)).thenReturn(
            Observable.error(
                Exception()
            )
        )

        `when`(mockConnectionManger.hasInternetConnection()).thenReturn(true)

        detailPresenter.attachView(mockDetailView)
        detailPresenter.getRepositoryDetails(FAKE_REPO_NAME, FAKE_REPO_OWNER_NAME)

        verify(mockDetailView).hideDetailText()
        verify(mockDetailView).hideSubscriberList()
        verify(mockDetailView).hideProgressBar()
        verify(mockDetailView).displayErrorMessage()
        verify(mockDetailView).showStatusTv()
    }

    companion object {
        internal const val FAKE_REPO_NAME = "fake repo"
        internal const val FAKE_REPO_OWNER_NAME = "fake name"
        internal const val FAKE_REPO_OWNER_AVATAR_URL = "fake name"
        internal const val FAKE_REPO_OWNER_BIO = "fake name"
    }
}