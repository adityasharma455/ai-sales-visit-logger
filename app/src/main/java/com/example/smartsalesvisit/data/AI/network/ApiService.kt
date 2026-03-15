package com.example.smartsalesvisit.data.AI.network

import com.example.smartsalesvisit.BuildConfig
import com.example.smartsalesvisit.domain.models.geminiResponse.GeminiRequest
import com.example.smartsalesvisit.domain.models.geminiResponse.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {

    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun generateVisitSummary(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Body request: GeminiRequest
    ): GeminiResponse
}


