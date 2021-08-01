package com.example.currencyappdb.networking

import com.example.currencyappdb.models.Currency
import com.example.currencyappdb.models.TimeModel
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("uzc/arkhiv-kursov-valyut/json/")
    fun getAllCurrencies():Call<List<Currency>>

    @GET("macros/echo?user_content_key=xPpS5_w7nh1em2_T3qElw9HkBsUrCd_2YnIcjw-n814lNKSGC1lS7mKPOrzDm_FNNCuKkSX1j0JC3fDpXwaToKuw8hsk6hVim5_BxDlH2jW0nuo2oDemN9CCS2h10ox_1xSncGQajx_ryfhECjZEnJ9GRkcRevgjTvo8Dc32iw_BLJPcPfRdVKhJT5HNzQuXEeN3QFwl2n0M6ZmO-h7C6bwVq0tbM60-uGhoxl1-0xY6ZWaEQ1joNfXott6NFs9j&lib=MwxUjRcLr2qLlnVOLh12wSNkqcO1Ikdrk")
    fun getTime():Call<TimeModel>


}