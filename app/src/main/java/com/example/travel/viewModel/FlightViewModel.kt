package com.example.travel.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travel.model.entities.flight.InstantScheduleResponse
import com.example.travel.model.repositories.FlightRepository
import com.example.travel.util.NetworkResult
import kotlinx.coroutines.launch

enum class AirFlyLine(val value: Int) {
    International(1), // 國際線
    Domestic(2), // 國內線
}

enum class AirFlyIO(val value: Int) {
    Departure(1), // 出發
    Arrival(2), // 抵達
}

class FlightViewModel(
    private val repository: FlightRepository
) : ViewModel() {
    private val _flightSchedules = MutableLiveData<NetworkResult<InstantScheduleResponse>>()
    val flightSchedules: LiveData<NetworkResult<InstantScheduleResponse>> = _flightSchedules

    /**
     * 獲取航班資訊
     *
     * @param airFlyLine 航班線路代碼
     * @param airFlyIO 航班進出代碼
     * @param showLoadingIndicator 是否顯示載入指示器
     */
    fun fetchFlightSchedules(
        airFlyLine: AirFlyLine,
        airFlyIO: AirFlyIO,
        showLoadingIndicator: Boolean = true
    ) {
        viewModelScope.launch {
            if (showLoadingIndicator || _flightSchedules.value == null || _flightSchedules.value !is NetworkResult.Success) {
                _flightSchedules.value = NetworkResult.Loading()
            }

            val newResult = repository.getFlightSchedules(airFlyLine.value, airFlyIO.value)

            // 避免在資料內容不變時觸發不必要的 LiveData 更新
            if (newResult is NetworkResult.Success && _flightSchedules.value is NetworkResult.Success) {
                val currentData = (_flightSchedules.value as NetworkResult.Success).data?.instantSchedule
                val newData = newResult.data?.instantSchedule

                // 比較新舊列表內容是否相同
                if (currentData == newData) {
                    return@launch
                }
            }

            _flightSchedules.value = newResult
        }
    }
}