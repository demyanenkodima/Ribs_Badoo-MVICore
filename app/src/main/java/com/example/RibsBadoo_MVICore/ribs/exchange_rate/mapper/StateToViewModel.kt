package com.example.RibsBadoo_MVICore.ribs.exchange_rate.mapper

import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.ExchangeRateView
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.feature.ExchangeRateFeature

internal object StateToViewModel : (ExchangeRateFeature.State) -> ExchangeRateView.ViewModel {
    override fun invoke(state: ExchangeRateFeature.State): ExchangeRateView.ViewModel =
        when (state) {
            is ExchangeRateFeature.State.FragmentsSet -> ExchangeRateView.ViewModel.LoadedFragments(
                state.fragments!!
            )
            is ExchangeRateFeature.State.InitialLoading -> ExchangeRateView.ViewModel.InitialLoading
            is ExchangeRateFeature.State.ExchangeRateToOne -> ExchangeRateView.ViewModel.ExchangeRateToOne(
                state.exchangeRateToOne
            )
            is ExchangeRateFeature.State.WalletSet -> ExchangeRateView.ViewModel.WalletSet(state.walletList)
            is ExchangeRateFeature.State.EnteredValue -> ExchangeRateView.ViewModel.EnteredValue
            is ExchangeRateFeature.State.CalculatedEnteredValue -> ExchangeRateView.ViewModel.CalculatedEnteredMoney(
                state.calculatedEnteredValueEnteredValue
            )
            is ExchangeRateFeature.State.IsViewPagersSwipeable -> ExchangeRateView.ViewModel.IsViewPagerSwipeable(
                state.isViewPagersSwipeable
            )
            is ExchangeRateFeature.State.ExchangeError -> ExchangeRateView.ViewModel.ExchangeError(state.throwable)
        }
}
