package com.example.RibsBadoo_MVICore.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.RibsBadoo_MVICore.common.retrofit.model.CurrencyExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.FragmentCurrencyType
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.concurrent.Callable

class CalculatorFeature :
    ActorReducerFeature<CalculatorFeature.Wish, CalculatorFeature.Effect, CalculatorFeature.State, CalculatorFeature.News>(
        initialState = State(),
        actor = ActorImpl(),
        reducer = ReducerImpl(),
        newsPublisher = NewsPublisherImpl()
    ) {

    data class State(
        val calculatedExchangeRate: CalculatedExchangeRate? = null,
        val calculatedEnteredValue: CalculatedEnteredValue? = null,
    )

    sealed class Wish {
        data class CalculateExchangeRateToOne(
            val currencySale: String,
            val currencyPurchase: String,
            val money: BigDecimal,
            val currencyExchangeRateList: List<CurrencyExchangeRate>
        ) : Wish()

        data class CalculateEnteredValue(
            val currencySale: String,
            val currencyPurchase: String,
            val money: BigDecimal,
            val currencyExchangeRateList: List<CurrencyExchangeRate>,
            val fragmentCurrencyType: FragmentCurrencyType
        ) : Wish()
    }

    sealed class Effect {
        data class CalculatedExchangeRateToOne(val calculatedExchangeRate: CalculatedExchangeRate) :
            Effect()

        data class CalculatedEnteredValue(val calculatedEnteredValue: com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue) :
            Effect()
    }

    sealed class News {
        data class CalculatedExchangeRateToOne(val calculatedExchangeRate: CalculatedExchangeRate) :
            News()

        data class CalculatedEnteredValue(val calculatedEnteredValue: com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue) : News()
    }

    class ActorImpl : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.CalculateExchangeRateToOne -> calculateExchangeRateToOne(wish)
                .map { Effect.CalculatedExchangeRateToOne(it) }
            is Wish.CalculateEnteredValue -> calculateEnteredValue(wish)
                .map { Effect.CalculatedEnteredValue(it) }
        }

        private fun calculateExchangeRateToOne(wish: Wish.CalculateExchangeRateToOne) =
            Observable.fromCallable(Callable {
                val (sale, purchase) = calculate(
                    currencyExchangeRateList = wish.currencyExchangeRateList,
                    currencySale = wish.currencySale,
                    currencyPurchase = wish.currencyPurchase,
                    money = wish.money
                )

                return@Callable CalculatedExchangeRate(
                    currencySale = wish.currencySale,
                    currencyPurchase = wish.currencyPurchase,
                    rateSale = sale,
                    ratePurchase = purchase
                )
            })

        private fun calculateEnteredValue(wish: Wish.CalculateEnteredValue) =
            Observable.fromCallable(Callable {
                val (sale, purchase) = calculate(
                    currencyExchangeRateList = wish.currencyExchangeRateList,
                    currencySale = wish.currencySale,
                    currencyPurchase = wish.currencyPurchase,
                    money = wish.money
                )
                val saleAndPurchase: Pair<BigDecimal, BigDecimal>
                when (wish.fragmentCurrencyType) {
                    FragmentCurrencyType.SALE -> {
                        saleAndPurchase = Pair(wish.money, sale)
                    }
                    FragmentCurrencyType.PURCHASE -> {
                        saleAndPurchase = Pair(purchase, wish.money)
                    }
                }
                return@Callable CalculatedEnteredValue(
                    CalculatedExchangeRate(
                        currencySale = wish.currencySale,
                        currencyPurchase = wish.currencyPurchase,
                        rateSale = sale,
                        ratePurchase = purchase
                    ),
                    amountForSale = saleAndPurchase.first,
                    amountForPurchase = saleAndPurchase.second
                )
            })

    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.CalculatedExchangeRateToOne -> state.copy()
            is Effect.CalculatedEnteredValue -> state.copy()
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.CalculatedExchangeRateToOne -> {
                News.CalculatedExchangeRateToOne(effect.calculatedExchangeRate)
            }
            is Effect.CalculatedEnteredValue -> {
                News.CalculatedEnteredValue(effect.calculatedEnteredValue)
            }
        }
    }
}

@Parcelize
data class CalculatedExchangeRate(
    val currencySale: String,
    val currencyPurchase: String,
    val rateSale: BigDecimal,
    val ratePurchase: BigDecimal
) : Parcelable

@Parcelize
data class CalculatedEnteredValue(
    val calculatedExchangeRate: CalculatedExchangeRate,
    val amountForSale: BigDecimal,
    val amountForPurchase: BigDecimal
) : Parcelable

private fun calculate(
    currencyExchangeRateList: List<CurrencyExchangeRate>,
    currencySale: String,
    currencyPurchase: String,
    money: BigDecimal
): Pair<BigDecimal, BigDecimal> {
    return Pair(
        convertCurrency(
            rateFrom = currencyExchangeRateList.first { it.name == currencySale }.rate,
            rateTo = currencyExchangeRateList.first { it.name == currencyPurchase }.rate,
            money = money
        ),
        convertCurrency(
            rateFrom = currencyExchangeRateList.first { it.name == currencyPurchase }.rate,
            rateTo = currencyExchangeRateList.first { it.name == currencySale }.rate,
            money = money
        )
    )
}

fun convertCurrency(
    rateFrom: BigDecimal,
    rateTo: BigDecimal,
    money: BigDecimal
): BigDecimal {
    // exchange money to base currency
    val inBaseCurrency = money.divide(rateFrom, MathContext.DECIMAL128)
    // exchange to target currency
    return rateTo.multiply(inBaseCurrency).setScale(2, RoundingMode.HALF_UP)
}