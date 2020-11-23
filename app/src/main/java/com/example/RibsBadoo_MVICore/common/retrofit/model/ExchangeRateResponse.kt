package com.example.RibsBadoo_MVICore.common.retrofit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

@Parcelize
data class ExchangeRateResponse(

    @SerializedName("rates") val rates: Rates,
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String
) : Parcelable

@Parcelize
data class Rates(
    @SerializedName(value = "RUB") val rub: BigDecimal,
    @SerializedName(value = "GBP") val gbp: BigDecimal,
    @SerializedName(value = "USD") val usd: BigDecimal,
) : Parcelable {
    fun getListOfProperties(): List<CurrencyExchangeRate> {
        // Because rates comes from the server as object, but I want List<T>
        val list: MutableList<CurrencyExchangeRate> = mutableListOf()
        this::class.memberProperties.forEach {
            if (it.visibility == KVisibility.PUBLIC) {
                val receivedCurrency = CurrencyExchangeRate(
                    it.name.toUpperCase(Locale.getDefault()),
                    it.getter.call(this).toString().toBigDecimal()
                )
                list.add(receivedCurrency)
            }
        }
        return list
    }
}

@Parcelize
data class CurrencyExchangeRate(val name: String, val rate: BigDecimal) : Parcelable