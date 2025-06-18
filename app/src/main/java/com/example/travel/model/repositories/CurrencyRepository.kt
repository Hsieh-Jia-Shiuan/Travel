package com.example.travel.model.repositories

import com.example.travel.model.dataSource.currency.CurrencyRemoteDataSource
import com.example.travel.model.entities.currency.CurrencyResponse
import com.example.travel.util.NetworkResult

class CurrencyRepository(
    private val remoteDataSource: CurrencyRemoteDataSource
) {
    suspend fun getLatestCurrencies(
        baseCurrency: String? = null,
        currencies: String? = null,
    ): NetworkResult<CurrencyResponse> {
        return remoteDataSource.getLatestCurrencies(baseCurrency, currencies)
    }
}