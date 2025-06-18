package com.example.travel.model.dataSource.flight

import com.example.travel.model.entities.flight.InstantScheduleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 航班相關的 API 服務
 */
interface FlightApiService {
    @GET("InstantSchedule.ashx")
    suspend fun getInstantSchedule(
        @Query("AirFlyLine") airFlyLine: Int,
        @Query("AirFlyIO") airFlyIO: Int
    ): Response<InstantScheduleResponse>
}
