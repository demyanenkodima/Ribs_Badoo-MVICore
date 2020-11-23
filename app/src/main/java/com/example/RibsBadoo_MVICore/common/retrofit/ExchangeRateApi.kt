package com.example.RibsBadoo_MVICore.common.retrofit

import com.example.RibsBadoo_MVICore.common.retrofit.model.ExchangeRateResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ExchangeRateApi {
    @GET("latest")
    fun getExchangeRate(): Observable<ExchangeRateResponse>

    companion object {
        private var retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io/")
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.createWithScheduler(
                    Schedulers.io()
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ExchangeRateApi = retrofit.create(ExchangeRateApi::class.java)
    }
}