package com.example.RibsBadoo_MVICore.ribs.exchange_rate.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.RibsBadoo_MVICore.common.retrofit.model.CurrencyExchangeRate
import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue
import com.example.RibsBadoo_MVICore.feature.CalculatedExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.CurrencyFragment
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.EnteredAmount
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.FragmentCurrencyType
import io.reactivex.Observable

class ExchangeRateFeature() :
    ActorReducerFeature<ExchangeRateFeature.Wish, ExchangeRateFeature.Effect, ExchangeRateFeature.State, ExchangeRateFeature.News>(
        initialState = State.InitialLoading,
        actor = ActorImpl(),
        reducer = ReducerImpl(),
        newsPublisher = NewsPublisherImpl()
    ) {


    sealed class State {
        object InitialLoading : State()
        data class FragmentsSet(
            val fragments: Pair<List<Pair<CurrencyFragment, String>>, List<Pair<CurrencyFragment, String>>>? = null
        ) : State()

        data class ExchangeRateToOne(val exchangeRateToOne: CalculatedExchangeRate) : State()
        data class WalletSet(val walletList: List<Wallet>?) : State()
        data class EnteredValue(val enteredValueText: EnteredAmount) : State()
        data class CalculatedEnteredValue(val calculatedEnteredValueEnteredValue: com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue) :
            State()

        data class IsViewPagersSwipeable(val isViewPagersSwipeable: Boolean) : State()
        data class ExchangeError(val throwable: Throwable) : State()
    }

    sealed class Wish {
        object ShowLoading : Wish()
        data class CreateFragmentsFrom(val currencyExchangeRateList: List<CurrencyExchangeRate>) :
            Wish()

        data class SetExchangeRateToOne(val calculatedExchangeRate: CalculatedExchangeRate) : Wish()
        data class SetWallet(val walletList: List<Wallet>) : Wish()
        data class UpdateViewPagerState(val viewpagerState: Pair<String, String>) : Wish()
        data class EnteredValue(val enteredValueText: EnteredAmount) : Wish()
        data class ShowCalculatedEnteredValue(val calculatedEnteredValue: CalculatedEnteredValue) :
            Wish()

        data class SetIsViewPagersSwipeable(val isSwipeable: Boolean) : Wish()
        data class ExchangeError(val throwable: Throwable) : Wish()
    }

    sealed class Effect {
        object StartLoading : Effect()
        data class CreatedFragments(
            val fragments: Pair<List<Pair<CurrencyFragment, String>>, List<Pair<CurrencyFragment, String>>>,
            val viewpagerState: Pair<String, String>
        ) : Effect()

        data class ViewPagerSwiped(val viewpagerState: Pair<String, String>) : Effect()

        data class ExchangeRateToOneSet(val calculatedExchangeRate: CalculatedExchangeRate) :
            Effect()

        data class SetWallet(val walletList: List<Wallet>) : Effect()
        data class EnteredValue(val enteredValueText: EnteredAmount) : Effect()
        data class CalculatedEnteredValue(val calculatedEnteredValueEnteredValueEnteredValueEnteredValueEnteredValue: com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue) :
            Effect()

        data class IsViewPagersSwipeableSet(val isSwipeable: Boolean) : Effect()
        data class ExchangeError(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class CreatedFragments(
            val fragments: Pair<List<Pair<CurrencyFragment, String>>, List<Pair<CurrencyFragment, String>>>,
            val viewpagerState: Pair<String, String>
        ) : News()

        data class OnViewPagerSwiped(val viewpagerState: Pair<String, String>) : News()
        data class EnteredValue(val enteredValueText: EnteredAmount) : News()
    }

    class ActorImpl : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.ShowLoading -> Observable.just(Effect.StartLoading)
            is Wish.CreateFragmentsFrom -> {
                Observable.just(
                    Effect.CreatedFragments(
                        Pair(
                            createFragments(
                                wish.currencyExchangeRateList,
                                FragmentCurrencyType.SALE
                            ),
                            createFragments(
                                wish.currencyExchangeRateList,
                                FragmentCurrencyType.PURCHASE
                            )
                        ),
                        Pair(
                            wish.currencyExchangeRateList[0].name,
                            wish.currencyExchangeRateList[0].name
                        )

                    )
                )
            }
            is Wish.UpdateViewPagerState -> {
                Observable.just(Effect.ViewPagerSwiped(wish.viewpagerState))
            }

            is Wish.SetExchangeRateToOne -> {
                Observable.just(Effect.ExchangeRateToOneSet(wish.calculatedExchangeRate))
            }
            is Wish.SetWallet -> {
                Observable.just(Effect.SetWallet(walletList = wish.walletList))
            }
            is Wish.EnteredValue -> {
                Observable.just(Effect.EnteredValue(wish.enteredValueText))
            }
            is Wish.ShowCalculatedEnteredValue -> {
                Observable.just(Effect.CalculatedEnteredValue(wish.calculatedEnteredValue))
            }
            is Wish.SetIsViewPagersSwipeable -> Observable.just(Effect.IsViewPagersSwipeableSet(wish.isSwipeable))
            is Wish.ExchangeError -> Observable.just(Effect.ExchangeError(wish.throwable))
        }

        private fun createFragments(
            currencyExchangeRateList: List<CurrencyExchangeRate>,
            fragmentCurrencyType: FragmentCurrencyType
        ): List<Pair<CurrencyFragment, String>> {
            val list: MutableList<Pair<CurrencyFragment, String>> = mutableListOf()
            currencyExchangeRateList.forEachIndexed { index, element ->
                list.add(
                    Pair(
                        CurrencyFragment.newInstance(
                            currency = element.name,
                            fragmentCurrencyType,
                        ), element.name
                    )
                )
            }
            return list
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.StartLoading -> State.InitialLoading
            is Effect.CreatedFragments -> {
                State.FragmentsSet(effect.fragments)
            }
            is Effect.ViewPagerSwiped -> State.InitialLoading
            is Effect.ExchangeRateToOneSet -> {
                State.ExchangeRateToOne(effect.calculatedExchangeRate)
            }
            is Effect.SetWallet -> {
                State.WalletSet(effect.walletList)
            }
            is Effect.EnteredValue -> {
                State.EnteredValue(effect.enteredValueText)
            }
            is Effect.CalculatedEnteredValue -> {
                State.CalculatedEnteredValue(effect.calculatedEnteredValueEnteredValueEnteredValueEnteredValueEnteredValue)
            }
            is Effect.IsViewPagersSwipeableSet -> {
                State.IsViewPagersSwipeable(effect.isSwipeable)
            }
            is Effect.ExchangeError -> State.ExchangeError(effect.throwable)
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.CreatedFragments -> {
                News.CreatedFragments(effect.fragments, effect.viewpagerState)
            }
            is Effect.ViewPagerSwiped -> {
                News.OnViewPagerSwiped(effect.viewpagerState)
            }
            is Effect.EnteredValue -> {
                News.EnteredValue(effect.enteredValueText)
            }
            else -> null
        }
    }
}