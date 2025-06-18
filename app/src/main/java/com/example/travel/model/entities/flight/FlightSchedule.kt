package com.example.travel.model.entities.flight

import com.google.gson.annotations.SerializedName

data class FlightSchedule(
    @SerializedName("expectTime")
    val expectTime: String,
    @SerializedName("realTime")
    val realTime: String,
    @SerializedName("airLineName")
    val airLineName: String,
    @SerializedName("airLineCode")
    val airLineCode: String,
    @SerializedName("airLineLogo")
    val airLineLogo: String,
    @SerializedName("airLineUrl")
    val airLineUrl: String,
    @SerializedName("airLineNum")
    val airLineNum: String,
    @SerializedName("upAirportCode")
    val upAirportCode: String?,
    @SerializedName("upAirportName")
    val upAirportName: String?,
    @SerializedName("goalAirportCode")
    val goalAirportCode: String?,
    @SerializedName("goalAirportName")
    val goalAirportName: String?,
    @SerializedName("airPlaneType")
    val airPlaneType: String,
    @SerializedName("airBoardingGate")
    val airBoardingGate: String,
    @SerializedName("airFlyStatus")
    val airFlyStatus: String,
    @SerializedName("airFlyDelayCause")
    val airFlyDelayCause: String
)