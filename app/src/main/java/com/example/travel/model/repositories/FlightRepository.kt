package com.example.travel.model.repositories

import com.example.travel.model.dataSource.flight.FlightRemoteDataSource
import com.example.travel.model.entities.flight.InstantScheduleResponse
import com.example.travel.util.NetworkResult

class FlightRepository(
    private val remoteDataSource: FlightRemoteDataSource
) {
    suspend fun getFlightSchedules(
        airFlyLine: Int,
        airFlyIO: Int
    ): NetworkResult<InstantScheduleResponse> {
        return remoteDataSource.getInstantSchedule(airFlyLine, airFlyIO)
    }
}