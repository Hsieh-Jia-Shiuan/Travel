package com.example.travel.model.dataSource

import com.example.travel.model.entities.InstantScheduleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("InstantSchedule.ashx")
    suspend fun getInstantSchedule(
        @Query("AirFlyLine") airFlyLine: Int,
        @Query("AirFlyIO") airFlyIO: Int
    ): Response<InstantScheduleResponse>
}