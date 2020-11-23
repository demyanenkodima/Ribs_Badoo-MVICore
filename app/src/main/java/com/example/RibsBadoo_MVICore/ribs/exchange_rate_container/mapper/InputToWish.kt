package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.mapper

import com.example.RibsBadoo_MVICore.common.retrofit.model.CurrencyExchangeRate
import com.example.RibsBadoo_MVICore.common.retrofit.model.ExchangeRateResponse
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.feature.GlobalStateFeature
import java.math.BigDecimal

internal object InputToWish :
        (ExchangeRateContainer.Input) -> GlobalStateFeature.Wish? {
    override fun invoke(news: ExchangeRateContainer.Input): GlobalStateFeature.Wish? =
        when (news) {
            is ExchangeRateContainer.Input.ResponseFromApi -> GlobalStateFeature.Wish.SetExchangeRateInfo(
                data = news.exchangeRateResponse.date,
                baseCurrency = news.exchangeRateResponse.base,
                currencyExchangeRateList = createExchangeRateListWithBaseCurrency(news.exchangeRateResponse)
            )
            is ExchangeRateContainer.Input.ExchangeRateToOne -> GlobalStateFeature.Wish.SetExchangeRateToOne(news.calculatedExchangeRate)
            is ExchangeRateContainer.Input.WalletSet -> GlobalStateFeature.Wish.SetWallet(news.walletList)
            is ExchangeRateContainer.Input.CalculatedEnteredValue -> GlobalStateFeature.Wish.SetCalculatedEnteredValue(news.calculatedEnteredValue)
            is ExchangeRateContainer.Input.ExchangeError -> GlobalStateFeature.Wish.ShowError(news.throwable)

        }

    private fun createExchangeRateListWithBaseCurrency(exchangeRateResponse: ExchangeRateResponse): List<CurrencyExchangeRate> {
        val baseRate = BigDecimal.ONE
        val currencyExchangeRateList: MutableList<CurrencyExchangeRate> = mutableListOf()
        currencyExchangeRateList.add(0, CurrencyExchangeRate(name = exchangeRateResponse.base, rate = baseRate))
        currencyExchangeRateList.addAll(exchangeRateResponse.rates.getListOfProperties())
        return currencyExchangeRateList
    }

}
