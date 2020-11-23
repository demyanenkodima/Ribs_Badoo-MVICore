package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.mapper

import com.example.RibsBadoo_MVICore.common.getExchangeRateToIne
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.FragmentCurrencyType
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.feature.GlobalStateFeature


internal object GlobalStateNewsToExchangeRateInput : (GlobalStateFeature.News) -> ExchangeRate.Input? {
    override fun invoke(news: GlobalStateFeature.News): ExchangeRate.Input? =
        when (news) {
            is GlobalStateFeature.News.ExchangeRateInfoSet -> ExchangeRate.Input.Fragments(news.currencyExchangeRateList)
            is GlobalStateFeature.News.ExchangeRateToOneSet -> ExchangeRate.Input.ExchangeRateToOne(
                news.exchangeRateToOne
            )
            is GlobalStateFeature.News.WalletSet -> ExchangeRate.Input.WalletSet(news.walletList)
            is GlobalStateFeature.News.CalculatedEnteredValueSet -> ExchangeRate.Input.CalculatedEnteredValue(news.calculatedEnteredValue)
            is GlobalStateFeature.News.ShowError -> ExchangeRate.Input.ExchangeError(news.throwable)
            else -> null
        }
}

internal object GlobalStateNewsToAppBarInput : (GlobalStateFeature.News) -> AppBar.Input? {
    override fun invoke(news: GlobalStateFeature.News): AppBar.Input? =
        when (news) {
            is GlobalStateFeature.News.ExchangeRateToOneSet -> AppBar.Input.Title(
                getExchangeRateToIne(
                    fragmentCurrencyType = FragmentCurrencyType.SALE,
                    exchangeRate = news.exchangeRateToOne
                )
            )
            else -> null
        }
}