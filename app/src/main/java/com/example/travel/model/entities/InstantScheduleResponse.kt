package com.example.travel.model.entities

import com.google.gson.annotations.SerializedName

data class InstantScheduleResponse(
    @SerializedName("InstantSchedule")
    val instantSchedule: List<FlightSchedule>
)