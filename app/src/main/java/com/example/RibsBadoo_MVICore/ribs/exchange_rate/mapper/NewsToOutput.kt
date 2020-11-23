package com.example.RibsBadoo_MVICore.ribs.exchange_rate.mapper

import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.feature.ExchangeRateFeature

internal object NewsToOutput : (ExchangeRateFeature.News) -> ExchangeRate.Output? {
    override fun invoke(news: ExchangeRateFeature.News): ExchangeRate.Output? =
        when (news) {
            is ExchangeRateFeature.News.CreatedFragments -> ExchangeRate.Output.SetViewPagerState(
                news.viewpagerState
            )
            is ExchangeRateFeature.News.OnViewPagerSwiped -> {
                ExchangeRate.Output.SetViewPagerState(news.viewpagerState)
            }
            is ExchangeRateFeature.News.EnteredValue -> ExchangeRate.Output.EnteredValue(news.enteredValueText)
        }
}
