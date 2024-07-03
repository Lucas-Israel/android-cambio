package com.betrybe.currencyview.data.api

import com.betrybe.currencyview.data.models.CurrencySymbolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("symbols")
    suspend fun getSymbols(
        @Header("apikey") apikey: String = "3xt1gldWPOEU3tAN7Ox3hrqTmzxxBPuT"
    ): Response<CurrencySymbolResponse>
}