package com.example.RibsBadoo_MVICore.ribs.exchange_rate.mapper

import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.feature.ExchangeRateFeature

internal object InputToWish : (ExchangeRate.Input) -> ExchangeRateFeature.Wish? {
    override fun invoke(news: ExchangeRate.Input): ExchangeRateFeature.Wish? =
        when (news) {
            is ExchangeRate.Input.Fragments -> ExchangeRateFeature.Wish.CreateFragmentsFrom(news.currencyExchangeRateList)
            is ExchangeRate.Input.ExchangeRateToOne -> ExchangeRateFeature.Wish.SetExchangeRateToOne(
                news.exchangeRateToOne
            )
            is ExchangeRate.Input.WalletSet -> ExchangeRateFeature.Wish.SetWallet(news.walletList)
            is ExchangeRate.Input.CalculatedEnteredValue -> ExchangeRateFeature.Wish.ShowCalculatedEnteredValue(
                news.calculatedEnteredValueEnteredValue
            )
            is ExchangeRate.Input.ExchangeError -> ExchangeRateFeature.Wish.ExchangeError(news.throwable)
        }
}
