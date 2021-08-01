package com.example.currencyappdb.models


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Currency(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("num")
    var num: Int,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("Ccy")
    var ccy: String? = null,
    @SerializedName("CcyNm_EN")
    var ccyNmEN: String? = null,
    @SerializedName("CcyNm_RU")
    var ccyNmRU: String? = null,
    @SerializedName("CcyNm_UZ")
    var ccyNmUZ: String? = null,
    @SerializedName("CcyNm_UZC")
    var ccyNmUZC: String? = null,
    @SerializedName("Code")
    var code: String? = null,
    @SerializedName("Date")
    var date: String? = null,
    @SerializedName("Diff")
    var diff: String? = null,
    @SerializedName("Nominal")
    var nominal: String? = null,
    @SerializedName("Rate")
    var rate: String? = null,
    @SerializedName("flag")
    var flag: Int? = null,
    @SerializedName("time")
    var time: String?  = ""

){
    companion object {
        @SerializedName("time")
        var onlineTime: String = ""
    }
}