package com.example.currencyapp.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private val retrofit by lazy{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        Retrofit.Builder()
            .baseUrl("http://cbu.uz/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
    }

    val api by lazy{
        retrofit.create(ApiService::class.java)
    }

    private val retrofitTime by lazy{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        Retrofit.Builder()
            .baseUrl("https://script.googleusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
    }

    val apiTime by lazy{
        retrofitTime.create(ApiService::class.java)
    }
}