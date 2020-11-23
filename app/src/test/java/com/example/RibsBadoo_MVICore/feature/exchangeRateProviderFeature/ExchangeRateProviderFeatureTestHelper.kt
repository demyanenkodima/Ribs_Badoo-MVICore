package com.example.RibsBadoo_MVICore.feature.exchangeRateProviderFeature

import com.example.RibsBadoo_MVICore.common.retrofit.model.ExchangeRateResponse
import com.example.RibsBadoo_MVICore.common.retrofit.model.Rates
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.math.BigDecimal
import java.net.HttpURLConnection

class ExchangeRateProviderFeatureTestHelper {
    companion object {
        val json = "{\"rates\":{\"CAD\":1.5484,\"HKD\":9.1972,\"ISK\":161.3,\"PHP\":57.206,\"DKK\":7.4489,\"HUF\":359.5,\"CZK\":26.34,\"AUD\":1.6227,\"RON\":4.8735,\"SEK\":10.2168,\"IDR\":16840.24,\"INR\":87.941,\"BRL\":6.3347,\"RUB\":90.2622,\"HRK\":7.5665,\"JPY\":123.18,\"THB\":35.922,\"CHF\":1.0811,\"SGD\":1.5934,\"PLN\":4.4639,\"BGN\":1.9558,\"TRY\":9.047,\"CNY\":7.7916,\"NOK\":10.6613,\"NZD\":1.7086,\"ZAR\":18.2192,\"USD\":1.1863,\"MXN\":23.8656,\"ILS\":3.9608,\"GBP\":0.89393,\"KRW\":1323.26,\"MYR\":4.8549},\"base\":\"EUR\",\"date\":\"2020-11-20\"}"
        val expectedTestResponse = ExchangeRateResponse(
            rates = Rates(
                rub = BigDecimal("90.2622"),
                gbp = BigDecimal("0.89393"),
                usd = BigDecimal("1.1863")
            ),
            base = "EUR",
            date = "2020-11-20"
        )
        val dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/latest" -> {
                        MockResponse()
                            .setResponseCode(HttpURLConnection.HTTP_OK)
                            .setBody(ExchangeRateProviderFeatureTestHelper.json)
                    }
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
    }
}
