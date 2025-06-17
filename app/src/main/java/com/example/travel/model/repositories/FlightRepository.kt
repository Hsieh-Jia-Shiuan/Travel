package com.example.travel.model.repositories

import com.example.travel.model.dataSource.RemoteDataSource
import com.example.travel.model.entities.InstantScheduleResponse
import com.example.travel.util.NetworkResult

class FlightRepository(
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getFlightSchedules(
        airFlyLine: Int,
        airFlyIO: Int
    ): NetworkResult<InstantScheduleResponse> {
        return remoteDataSource.getInstantSchedule(airFlyLine, airFlyIO)
    }
}