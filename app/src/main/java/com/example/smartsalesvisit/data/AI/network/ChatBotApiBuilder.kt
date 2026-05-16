package com.example.smartsalesvisit.data.AI.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChatBotApiBuilder {

    private const val BASE_URL = "http://10.196.84.57:8000/" // 🔥 change this

    val api: ChatBotApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatBotApiService::class.java)
    }
}