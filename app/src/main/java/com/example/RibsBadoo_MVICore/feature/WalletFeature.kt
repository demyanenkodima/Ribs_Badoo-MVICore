package com.example.RibsBadoo_MVICore.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.RibsBadoo_MVICore.common.room.WalletDao
import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.concurrent.Callable

class WalletFeature(walletDao: WalletDao) :
    ActorReducerFeature<WalletFeature.Wish, WalletFeature.Effect, WalletFeature.State, WalletFeature.News>(
        initialState = State(),
        actor = ActorImpl(walletDao),
        reducer = ReducerImpl(),
        newsPublisher = NewsPublisherImpl(),
    ) {

    data class State(
        val walletList: List<Wallet>? = null
    )

    sealed class Wish {
        object LoadWallet : Wish()
        data class Exchange(val calculatedEnteredValue: CalculatedEnteredValue) : Wish()
    }

    sealed class Effect {
        data class LoadedWallet(val walletList: List<Wallet>) : Effect()
        object Exchanged : Effect()
        data class ExchangeError(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class LoadedWallet(val walletList: List<Wallet>) : News()
        data class Exchanged(val walletList: List<Wallet>) : News()
        data class ExchangeError(val throwable: Throwable) : News()
    }

    class ActorImpl(private val walletDao: WalletDao) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.LoadWallet -> {
                getDatabase().observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .subscribeOn(Schedulers.io())
                    .map { Effect.LoadedWallet(walletList = it) }
            }
            is Wish.Exchange -> calculateExchangeRateToOne(wish, state.walletList!!)
                .map {
                    Effect.Exchanged as Effect
                }
                .onErrorReturn {
                    Effect.ExchangeError(it)
                }
        }

        private fun getDatabase(): Observable<List<Wallet>> =
            walletDao.getAll().flatMapObservable { walletList -> Observable.just(walletList) }

        private fun calculateExchangeRateToOne(wish: Wish.Exchange, walletList: List<Wallet>) =
            // TODO refactoring!!!
            Observable.fromCallable(Callable {
                val walletSoldCurrency =
                    walletList.first { it.currency == wish.calculatedEnteredValue.calculatedExchangeRate.currencySale }
                val walletPurchasedCurrency =
                    walletList.first { it.currency == wish.calculatedEnteredValue.calculatedExchangeRate.currencyPurchase }
                if (walletSoldCurrency.balance.minus(wish.calculatedEnteredValue.amountForSale) < BigDecimal.ZERO) {
                    throw Throwable("Insufficient funds for replenishment")
                } else {
                    // Sale
                    walletSoldCurrency.balance -= wish.calculatedEnteredValue.amountForSale
                    // Purchase
                    walletPurchasedCurrency.balance =
                        walletPurchasedCurrency.balance.plus(wish.calculatedEnteredValue.amountForPurchase)
                }

                return@Callable walletDao.update(walletSoldCurrency)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnError { Effect.ExchangeError(it) }
                    .andThen(walletDao.update(walletPurchasedCurrency)
                        .doOnError { Effect.ExchangeError(it) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                    )
                    .andThen(Observable.just(Effect.Exchanged))
                    .subscribe()
            })
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.LoadedWallet -> state.copy(
                walletList = effect.walletList
            )
            is Effect.Exchanged -> state.copy()
            is Effect.ExchangeError -> state.copy()
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.LoadedWallet -> News.LoadedWallet(effect.walletList)
            is Effect.Exchanged -> News.Exchanged(state.walletList!!)
            is Effect.ExchangeError -> News.ExchangeError(effect.throwable)
        }
    }
}