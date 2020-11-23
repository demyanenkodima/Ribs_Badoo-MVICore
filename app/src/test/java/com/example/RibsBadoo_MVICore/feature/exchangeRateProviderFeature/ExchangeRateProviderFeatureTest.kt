package com.example.RibsBadoo_MVICore.feature.exchangeRateProviderFeature

import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.RibsBadoo_MVICore.RxTrampolineSchedulerRule
import com.example.RibsBadoo_MVICore.common.retrofit.ExchangeRateApi
import com.example.RibsBadoo_MVICore.feature.ExchangeRateProviderFeature
import com.example.RibsBadoo_MVICore.onNextEvents
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert.*
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeRateProviderFeatureTest {
    @Rule
    @JvmField
    var testSchedulerRule = RxTrampolineSchedulerRule()

    private lateinit var feature: ActorReducerFeature<ExchangeRateProviderFeature.Wish, ExchangeRateProviderFeature.Effect, ExchangeRateProviderFeature.State, ExchangeRateProviderFeature.News>
    private lateinit var states: TestObserver<ExchangeRateProviderFeature.State>
    private lateinit var newsSubject: PublishSubject<ExchangeRateProviderFeature.News>

    private var mockWebServer = MockWebServer()
    private lateinit var exchangeRateApi: ExchangeRateApi

    @Before
    fun prepare() {
        setupMock()
        setupFeature()
    }

    private fun setupMock() {
        mockWebServer.dispatcher = ExchangeRateProviderFeatureTestHelper.dispatcher
        mockWebServer.start()
        exchangeRateApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.createWithScheduler(
                    Schedulers.io()
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }

    private fun setupFeature() {
        newsSubject = PublishSubject.create<ExchangeRateProviderFeature.News>()
        feature = ActorReducerFeature(
            initialState = ExchangeRateProviderFeature.State(),
            actor = ExchangeRateProviderFeature.ActorImpl(exchangeRateApi = exchangeRateApi),
            reducer = ExchangeRateProviderFeature.ReducerImpl(),
            newsPublisher = ExchangeRateProviderFeature.NewsPublisherImpl(),
            bootstrapper = ExchangeRateProviderFeature.BootstrapperImpl()
        )

        val subscription = PublishSubject.create<ExchangeRateProviderFeature.State>()
        states = subscription.test()
        feature.subscribe(subscription)
        feature.news.subscribe(newsSubject)
    }

    @Test
    fun `emitted initial state is correct`() {
        // TODO for some reason I do not receive news
        newsSubject.subscribe {
            when (it) {
                is ExchangeRateProviderFeature.News.LoadedExchangeRate -> {
                    assertEquals(it.exchangeRateResponse, ExchangeRateProviderFeatureTestHelper.expectedTestResponse)
                }
            }
        }

        val state: ExchangeRateProviderFeature.State =
            states.onNextEvents().first() as ExchangeRateProviderFeature.State
        assertEquals(state.exchangeRateResponse, ExchangeRateProviderFeatureTestHelper.expectedTestResponse)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}