package com.example.travel.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travel.model.entities.currency.CurrencyResponse
import com.example.travel.model.repositories.CurrencyRepository
import com.example.travel.util.NetworkResult
import kotlinx.coroutines.launch

class CurrencyViewModel(
    private val repository: CurrencyRepository
) : ViewModel() {
    private val _currencyList = MutableLiveData<NetworkResult<CurrencyResponse>>()
    val currencyList: LiveData<NetworkResult<CurrencyResponse>> = _currencyList

    private var _cachedCurrencyData: Map<String, Double>? = null

    /**
     * 獲取最新的貨幣匯率清單。
     * @param baseCurrency 基礎貨幣, 預設為USD。
     * @param currencies 以逗號分隔的貨幣代碼字串（例如 "EUR,JPY,TWD"）。
     */
    fun fetchLatestCurrencies(baseCurrency: String? = null, currencies: String? = null) {
        _currencyList.value = NetworkResult.Loading()

        viewModelScope.launch {
            val result = repository.getLatestCurrencies(
                baseCurrency = baseCurrency,
                currencies = currencies
            )
            _currencyList.value = result
        }
    }

//    /**
//     * 切換基礎貨幣，無需重新進行網路請求。
//     */
//    fun updateBaseCurrency(newBaseCurrency: String) {
//        val cachedData = _cachedCurrencyData
//
//        if (cachedData == null || cachedData.isEmpty()) {
//            _currencyList.value = NetworkResult.Error("Currency data not loaded or is empty. Please refresh.")
//            return
//        }
//
//        val newBaseRateAgainstOriginal = cachedData[newBaseCurrency]
//
//        if (newBaseRateAgainstOriginal == null || newBaseRateAgainstOriginal == 0.0) {
//            _currencyList.value = NetworkResult.Error("Invalid new base currency selected or its rate is zero.")
//            return
//        }
//
//        val convertedMap = mutableMapOf<String, Double>()
//        for ((currencyCode, rate) in cachedData) {
//            convertedMap[currencyCode] = rate / newBaseRateAgainstOriginal
//        }
//
//        _currencyList.value = NetworkResult.Success(CurrencyResponse(data = convertedMap))
//    }
}