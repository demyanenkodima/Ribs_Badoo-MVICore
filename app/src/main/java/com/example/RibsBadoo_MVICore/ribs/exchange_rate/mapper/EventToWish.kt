package com.example.RibsBadoo_MVICore.ribs.exchange_rate.mapper

import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.ExchangeRateView
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.event.ExchangeRateEvent
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.feature.ExchangeRateFeature

internal object UiEventToWish : (ExchangeRateView.Event) -> ExchangeRateFeature.Wish? {
    override fun invoke(event: ExchangeRateView.Event): ExchangeRateFeature.Wish? =
        when (event) {
            is ExchangeRateView.Event.OnViewPagerSwiped -> ExchangeRateFeature.Wish.UpdateViewPagerState(
                event.viewPagersState
            )
        }
}

internal object ExchangeRateEventToWish : (ExchangeRateEvent) -> ExchangeRateFeature.Wish? {
    override fun invoke(event: ExchangeRateEvent): ExchangeRateFeature.Wish? =
        when (event) {
            is ExchangeRateEvent.SetEnteredAmount -> ExchangeRateFeature.Wish.EnteredValue(event.enteredAmount)
            is ExchangeRateEvent.SetIsViewPagersSwipeable -> ExchangeRateFeature.Wish.SetIsViewPagersSwipeable(
                event.isSwipeable
            )
            else -> null
        }
}
