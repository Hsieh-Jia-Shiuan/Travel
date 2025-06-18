package com.example.travel.model.entities.currency

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("data")
    val data: Map<String, Double>
)
