package com.example.RibsBadoo_MVICore.ribs.root.mapper

import com.example.RibsBadoo_MVICore.feature.CalculatorFeature
import com.example.RibsBadoo_MVICore.feature.ExchangeRateProviderFeature
import com.example.RibsBadoo_MVICore.feature.WalletFeature
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer

internal object ExchangeRateProvideFeatureNewsToExchangeRateContainerInput :
        (ExchangeRateProviderFeature.News) -> ExchangeRateContainer.Input? {
    override fun invoke(news: ExchangeRateProviderFeature.News): ExchangeRateContainer.Input? =
        when (news) {
            is ExchangeRateProviderFeature.News.LoadedExchangeRate -> ExchangeRateContainer.Input.ResponseFromApi(
                news.exchangeRateResponse
            )
            else -> null
        }
}

internal object CalculatorFeatureNewsToExchangeRateContainerInput :
        (CalculatorFeature.News) -> ExchangeRateContainer.Input? {
    override fun invoke(news: CalculatorFeature.News): ExchangeRateContainer.Input? =
        when (news) {
            is CalculatorFeature.News.CalculatedExchangeRateToOne -> ExchangeRateContainer.Input.ExchangeRateToOne(
                news.calculatedExchangeRate
            )
            is CalculatorFeature.News.CalculatedEnteredValue -> ExchangeRateContainer.Input.CalculatedEnteredValue(
                news.calculatedEnteredValue
            )
        }
}

internal object WalletFeatureNewsToExchangeRateContainerInput :
        (WalletFeature.News) -> ExchangeRateContainer.Input? {
    override fun invoke(news: WalletFeature.News): ExchangeRateContainer.Input? =
        when (news) {
            is WalletFeature.News.LoadedWallet -> ExchangeRateContainer.Input.WalletSet(news.walletList)
            is WalletFeature.News.Exchanged -> ExchangeRateContainer.Input.WalletSet(news.walletList)
            is WalletFeature.News.ExchangeError -> ExchangeRateContainer.Input.ExchangeError(news.throwable)
        }
}


