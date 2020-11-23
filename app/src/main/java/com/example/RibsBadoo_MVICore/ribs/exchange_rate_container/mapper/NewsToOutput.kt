package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.mapper

import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.feature.GlobalStateFeature

internal object NewsToOutput :
        (GlobalStateFeature.News) -> ExchangeRateContainer.Output? {
    override fun invoke(news: GlobalStateFeature.News): ExchangeRateContainer.Output? =
        when (news) {
            is GlobalStateFeature.News.SendToCalculateExchangeRateToOne -> ExchangeRateContainer.Output.CalculateExchangeRateToOne(
                currencySale = news.currencySale,
                currencyPurchase = news.currencyPurchase,
                money = news.money,
                currencyExchangeRateList = news.currencyExchangeRateList
            )
            is GlobalStateFeature.News.SendToCalculateEnteredValue -> ExchangeRateContainer.Output.CalculateEnteredValue(
                currencySale = news.currencySale,
                currencyPurchase = news.currencyPurchase,
                money = news.money,
                currencyExchangeRateList = news.currencyExchangeRateList,
                fragmentCurrencyType = news.fragmentCurrencyType
            )
            is GlobalStateFeature.News.Exchange -> ExchangeRateContainer.Output.Exchange(news.calculatedEnteredValue)
            else -> null
        }
}
