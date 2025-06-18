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
     */
    fun fetchFlightSchedules(airFlyLine: AirFlyLine, airFlyIO: AirFlyIO) {
        viewModelScope.launch {
            _flightSchedules.value = NetworkResult.Loading()
            _flightSchedules.value = repository.getFlightSchedules(airFlyLine.value, airFlyIO.value)
        }
    }
}