package com.example.currencyappdb.models


import com.google.gson.annotations.SerializedName

data class TimeModel(
    @SerializedName("hours")
    var hours: Int?,
    @SerializedName("minutes")
    var minutes: Int?
)