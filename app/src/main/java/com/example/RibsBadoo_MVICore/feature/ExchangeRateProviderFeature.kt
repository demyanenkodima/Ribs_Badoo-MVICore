package com.example.RibsBadoo_MVICore.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.RibsBadoo_MVICore.common.retrofit.ExchangeRateApi
import com.example.RibsBadoo_MVICore.common.retrofit.model.ExchangeRateResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExchangeRateProviderFeature(exchangeRateApi: ExchangeRateApi) :
    ActorReducerFeature<ExchangeRateProviderFeature.Wish, ExchangeRateProviderFeature.Effect, ExchangeRateProviderFeature.State, ExchangeRateProviderFeature.News>(
        initialState = State(),
        actor = ActorImpl(exchangeRateApi),
        reducer = ReducerImpl(),
        newsPublisher = NewsPublisherImpl(),
        bootstrapper = BootstrapperImpl()
    ) {

    data class State(
        val isLoading: Boolean = false,
        val exchangeRateResponse: ExchangeRateResponse? = null
    )

    sealed class Wish {
        object LoadExchangeRate : Wish()
    }

    sealed class Effect {
        object StartedLoading : Effect()
        data class FinishedWithSuccess(val exchangeRateResponse: ExchangeRateResponse) : Effect()
        data class FinishedWithError(val throwable: Throwable) : Effect()
    }

    sealed class News {
        object StartedLoading : News()
        data class LoadedExchangeRate(val exchangeRateResponse: ExchangeRateResponse) : News()
        data class ErrorExecutingRequest(val throwable: Throwable) : News()
    }

    class BootstrapperImpl : Bootstrapper<Wish> {
        override fun invoke(): Observable<Wish> = Observable.just(Wish.LoadExchangeRate)
    }

    class ActorImpl(private val exchangeRateApi: ExchangeRateApi) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.LoadExchangeRate -> {
                if (!state.isLoading) {
                    exchangeRateApi.getExchangeRate()
                        .cache()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { Effect.FinishedWithSuccess(exchangeRateResponse = it) as Effect }
                        .startWith(Effect.StartedLoading)
                        .onErrorReturn { Effect.FinishedWithError(it) }
                } else {
                    Observable.empty()
                }
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.StartedLoading -> state.copy(
                isLoading = true
            )
            is Effect.FinishedWithSuccess -> state.copy(
                isLoading = false,
                exchangeRateResponse = effect.exchangeRateResponse
            )
            is Effect.FinishedWithError -> state.copy(
                isLoading = false
            )
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.StartedLoading -> News.StartedLoading
            is Effect.FinishedWithSuccess -> News.LoadedExchangeRate(exchangeRateResponse = effect.exchangeRateResponse)
            is Effect.FinishedWithError -> News.ErrorExecutingRequest(effect.throwable)
        }
    }
}