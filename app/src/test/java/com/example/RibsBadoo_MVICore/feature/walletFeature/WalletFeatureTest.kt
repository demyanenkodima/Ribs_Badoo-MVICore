package com.example.RibsBadoo_MVICore.feature.walletFeature

import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.RibsBadoo_MVICore.RxTrampolineSchedulerRule
import com.example.RibsBadoo_MVICore.common.room.WalletRepository
import com.example.RibsBadoo_MVICore.common.room.WalletDao
import com.example.RibsBadoo_MVICore.feature.WalletFeature
import com.example.RibsBadoo_MVICore.onNextEvents
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class WalletFeatureTest {
    @Rule
    @JvmField
    var testSchedulerRule = RxTrampolineSchedulerRule()

    private val walletRepositoryMock: WalletRepository = Mockito.mock(WalletRepository::class.java)
    private val walletDaoMock: WalletDao = Mockito.mock(WalletDao::class.java)

    private lateinit var feature: ActorReducerFeature<WalletFeature.Wish, WalletFeature.Effect, WalletFeature.State, WalletFeature.News>
    private lateinit var states: TestObserver<WalletFeature.State>
    private lateinit var newsSubject: PublishSubject<WalletFeature.News>

    @Before
    fun prepare() {
        setupMock()
        setupFeature()
    }

    private fun setupMock(){
        Mockito.`when`(walletRepositoryMock.productsDao()).thenReturn(walletDaoMock)
        Mockito.`when`(walletDaoMock.getAll())
            .thenReturn(Maybe.just(WalletFeatureTestHelper.walletList))
        Mockito.`when`(walletDaoMock.update(any()))
            .thenReturn(Completable.complete())
    }

    private fun setupFeature(){
        newsSubject = PublishSubject.create()
        feature = ActorReducerFeature(
            initialState = WalletFeature.State(),
            actor = WalletFeature.ActorImpl(walletDaoMock),
            reducer = WalletFeature.ReducerImpl(),
            newsPublisher = WalletFeature.NewsPublisherImpl()
        )

        val subscription = PublishSubject.create<WalletFeature.State>()
        states = subscription.test()
        feature.subscribe(subscription)
        feature.news.subscribe(newsSubject)
    }

    @Test
    fun `emitted initial state is correct`() {
        val state: WalletFeature.State = states.onNextEvents().first() as WalletFeature.State
        assertNull(state.walletList)
    }

    @Test
    fun `when send wish LoadWallet - set walletList to State and send LoadedWallet news`() {
        newsSubject.subscribe {
            when (it) {
                is WalletFeature.News.LoadedWallet -> {
                    assertEquals(it.walletList, WalletFeatureTestHelper.walletList)
                }
            }
        }

        feature.accept(WalletFeature.Wish.LoadWallet)
        val state = states.onNextEvents().last() as WalletFeature.State
        assertNotNull(state.walletList)
    }

    @Test
    fun `when send wish Exchange - sale 1 EUR and purchase 90,26 RUB`() {
        newsSubject.subscribe {
            when (it) {
                is WalletFeature.News.Exchanged -> {
                    assertEquals(it.walletList, WalletFeatureTestHelper.walletListAfterExchange)
                }
            }
        }

        `when send wish LoadWallet - set walletList to State and send LoadedWallet news`()

        feature.accept(WalletFeature.Wish.Exchange(WalletFeatureTestHelper.enteredAmount))
        val state = states.onNextEvents().last() as WalletFeature.State
        assertEquals(state.walletList, WalletFeatureTestHelper.walletListAfterExchange)
    }
}