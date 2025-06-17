package com.example.travel.model.dataSource

import com.example.travel.model.entities.InstantScheduleResponse
import com.example.travel.util.NetworkResult
import retrofit2.HttpException
import java.io.IOException

class RemoteDataSource(private val apiService: ApiService) {
    /**
     * 獲取即時航班資訊
     */
    suspend fun getInstantSchedule(
        airFlyLine: Int,
        airFlyIO: Int
    ): NetworkResult<InstantScheduleResponse> {
        return try {
            val response = apiService.getInstantSchedule(airFlyLine, airFlyIO)
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