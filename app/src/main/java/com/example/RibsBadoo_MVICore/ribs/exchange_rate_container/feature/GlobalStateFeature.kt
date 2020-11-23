package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.RibsBadoo_MVICore.common.getMoneyPrefixToView
import com.example.RibsBadoo_MVICore.common.retrofit.model.CurrencyExchangeRate
import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue
import com.example.RibsBadoo_MVICore.feature.CalculatedExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.FragmentCurrencyType
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

class GlobalStateFeature(timeCapsule: TimeCapsule<Parcelable>? = null) :
    ActorReducerFeature<GlobalStateFeature.Wish, GlobalStateFeature.Effect, GlobalStateFeature.State, GlobalStateFeature.News>(
        initialState = timeCapsule?.get(GlobalStateFeature::class.java) ?: State(),
        actor = ActorImpl(),
        reducer = ReducerImpl(),
        newsPublisher = NewsPublisherImpl()
    ) {

    init {
        timeCapsule?.register(GlobalStateFeature::class.java) { state.copy() }
    }

    @Parcelize
    data class State(
        val data: String? = null,
        val baseCurrency: String? = null,
        val currencyExchangeRateList: List<CurrencyExchangeRate>? = null,
        val currentCurrencies: Pair<String, String>? = null,
        val exchangeRateToOne: CalculatedExchangeRate? = null,
        val walletList: List<Wallet>? = null,
        val calculatedEnteredValue: CalculatedEnteredValue? = null,
    ) : Parcelable

    sealed class Wish {
        data class SetExchangeRateInfo(
            val data: String,
            val baseCurrency: String,
            val currencyExchangeRateList: List<CurrencyExchangeRate>
        ) : Wish()

        data class SetCurrentCurrencies(val currentCurrencies: Pair<String, String>) : Wish()
        data class SetExchangeRateToOne(val exchangeRateToOne: CalculatedExchangeRate) : Wish()
        data class SetWallet(val walletList: List<Wallet>) : Wish()

        data class CalculateEnteredValue(
            val value: String,
            val fragmentCurrencyType: FragmentCurrencyType
        ) : Wish()

        data class SetCalculatedEnteredValue(val calculatedEnteredValue: CalculatedEnteredValue) :
            Wish()

        object Exchange : Wish()
        data class ShowError(val throwable: Throwable) : Wish()
    }

    sealed class Effect {
        data class ExchangeRateInfoSet(
            val data: String,
            val baseCurrency: String,
            val currencyExchangeRateList: List<CurrencyExchangeRate>
        ) : Effect()

        data class SendToCalculateExchangeRateToOne(val currentCurrencies: Pair<String, String>) :
            Effect()

        data class ExchangeRateToOneSet(val exchangeRateToOne: CalculatedExchangeRate) : Effect()
        data class WalletSet(val walletList: List<Wallet>) : Effect()

        data class SendToCalculateEnteredValue(
            val value: String,
            val fragmentCurrencyType: FragmentCurrencyType
        ) : Effect()

        data class CalculatedEnteredValueSet(val calculatedEnteredValue: CalculatedEnteredValue) :
            Effect()

        data class Exchange(val calculatedEnteredValue: CalculatedEnteredValue) : Effect()
        data class ShowError(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ExchangeRateInfoSet(
            val data: String,
            val baseCurrency: String,
            val currencyExchangeRateList: List<CurrencyExchangeRate>
        ) : News()

        data class SendToCalculateExchangeRateToOne(
            val currencySale: String,
            val currencyPurchase: String,
            val money: BigDecimal,
            val currencyExchangeRateList: List<CurrencyExchangeRate>
        ) : News()

        data class ExchangeRateToOneSet(val exchangeRateToOne: CalculatedExchangeRate) : News()
        data class WalletSet(val walletList: List<Wallet>) : News()

        data class SendToCalculateEnteredValue(
            val currencySale: String,
            val currencyPurchase: String,
            val money: BigDecimal,
            val currencyExchangeRateList: List<CurrencyExchangeRate>,
            val fragmentCurrencyType: FragmentCurrencyType
        ) : News()

        data class CalculatedEnteredValueSet(val calculatedEnteredValue: CalculatedEnteredValue) :
            News()

        data class Exchange(val calculatedEnteredValue: CalculatedEnteredValue) : News()
        data class ShowError(val throwable: Throwable) : News()
    }

    class ActorImpl : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.SetExchangeRateInfo -> Observable.just(
                Effect.ExchangeRateInfoSet(
                    data = wish.data,
                    baseCurrency = wish.baseCurrency,
                    currencyExchangeRateList = wish.currencyExchangeRateList
                )
            )
            is Wish.SetCurrentCurrencies ->
                Observable.just(Effect.SendToCalculateExchangeRateToOne(wish.currentCurrencies))
            is Wish.SetWallet -> Observable.just(Effect.WalletSet(wish.walletList))
            is Wish.CalculateEnteredValue ->
                Observable.just(
                    Effect.SendToCalculateEnteredValue(wish.value, wish.fragmentCurrencyType)
                )
            is Wish.SetCalculatedEnteredValue -> Observable.just(
                Effect.CalculatedEnteredValueSet(
                    wish.calculatedEnteredValue
                )
            )
            is Wish.Exchange -> Observable.just(Effect.Exchange(state.calculatedEnteredValue!!))
            is Wish.SetExchangeRateToOne -> Observable.just(Effect.ExchangeRateToOneSet(wish.exchangeRateToOne))
            is Wish.ShowError -> Observable.just(Effect.ShowError(wish.throwable))
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.ExchangeRateInfoSet -> state.copy(
                data = effect.data,
                baseCurrency = effect.baseCurrency,
                currencyExchangeRateList = effect.currencyExchangeRateList
            )
            is Effect.SendToCalculateExchangeRateToOne -> state.copy(
                currentCurrencies = effect.currentCurrencies
            )
            is Effect.WalletSet -> state.copy(
                walletList = effect.walletList
            )
            is Effect.SendToCalculateEnteredValue -> state.copy()
            is Effect.CalculatedEnteredValueSet -> state.copy(
                calculatedEnteredValue = effect.calculatedEnteredValue
            )
            is Effect.Exchange -> state.copy()
            is Effect.ExchangeRateToOneSet -> {
                state.copy(exchangeRateToOne = effect.exchangeRateToOne)
            }
            is Effect.ShowError -> state.copy()
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.ExchangeRateInfoSet -> News.ExchangeRateInfoSet(
                data = effect.data,
                baseCurrency = effect.baseCurrency,
                currencyExchangeRateList = effect.currencyExchangeRateList
            )

            is Effect.SendToCalculateExchangeRateToOne -> News.SendToCalculateExchangeRateToOne(
                currencySale = state.currentCurrencies!!.first,
                currencyPurchase = state.currentCurrencies.second,
                money = getMoneyPrefixToView(),
                currencyExchangeRateList = state.currencyExchangeRateList!!
            )
            is Effect.WalletSet -> News.WalletSet(effect.walletList)
            is Effect.SendToCalculateEnteredValue -> News.SendToCalculateEnteredValue(
                currencySale = state.currentCurrencies!!.first,
                currencyPurchase = state.currentCurrencies.second,
                money = BigDecimal(effect.value),
                currencyExchangeRateList = state.currencyExchangeRateList!!,
                fragmentCurrencyType = effect.fragmentCurrencyType
            )
            is Effect.Exchange -> News.Exchange(effect.calculatedEnteredValue)
            is Effect.ExchangeRateToOneSet -> News.ExchangeRateToOneSet(effect.exchangeRateToOne)
            is Effect.CalculatedEnteredValueSet -> News.CalculatedEnteredValueSet(effect.calculatedEnteredValue)
            is Effect.ShowError -> News.ShowError(effect.throwable)
        }
    }
}