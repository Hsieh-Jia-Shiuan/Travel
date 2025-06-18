package com.example.travel.model.dataSource.currency

import com.example.travel.model.entities.currency.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 貨幣匯率相關的 API 服務
 */
interface CurrencyApiService {
    @GET("v1/latest")
    suspend fun getLatestCurrencies(
        @Query("apikey") apiKey: String,
        @Query("base_currency") baseCurrency: String? = null,
        @Query("currencies") currencies: String? = null
    ): Response<CurrencyResponse>
}
