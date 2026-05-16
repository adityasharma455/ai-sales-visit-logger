package com.example.smartsalesvisit.data.AI.network


import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SarvamApiBuilder {



    private const val API_KEY = "sk_hb89y9sb_ghB0t3Mwczm9NLbfIRljruWS"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->

            val request: Request = chain.request().newBuilder()
                .addHeader("api-subscription-key", API_KEY)
                .build()

            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.sarvam.ai/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: SarvamApiService by lazy {
        retrofit.create(SarvamApiService::class.java)
    }
}