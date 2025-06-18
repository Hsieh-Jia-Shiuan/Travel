package com.example.travel.model.dataSource.currency

import com.example.travel.model.entities.currency.CurrencyResponse
import com.example.travel.util.NetworkResult
import retrofit2.HttpException
import java.io.IOException

/**
 * 處理貨幣相關的遠程數據源操作
 */
class CurrencyRemoteDataSource(
    private val apiKey: String,
    private val currencyApiService: CurrencyApiService
) {
    /**
     * 獲取貨幣匯率
     */
    suspend fun getLatestCurrencies(
        baseCurrency: String?,
        currencies: String?
    ): NetworkResult<CurrencyResponse> {
        return try {
            val response = currencyApiService.getLatestCurrencies(apiKey, baseCurrency, currencies)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResult.Success(it)
                } ?: NetworkResult.Error("API response body is null.")
            } else {
                NetworkResult.Error("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: HttpException) {
            NetworkResult.Error("Network error: ${e.message()}")
        } catch (e: IOException) {
            NetworkResult.Error("No internet connection. Please check your network settings.")
        } catch (e: Exception) {
            NetworkResult.Error("An unexpected error occurred: ${e.message ?: "Unknown error"}")
        }
    }
}
