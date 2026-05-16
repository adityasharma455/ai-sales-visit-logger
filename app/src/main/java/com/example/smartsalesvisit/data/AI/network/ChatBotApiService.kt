package com.example.smartsalesvisit.data.AI.network

import com.example.smartsalesvisit.domain.models.chatBot.ChatRequest
import com.example.smartsalesvisit.domain.models.chatBot.ChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatBotApiService {

    @POST("/chat")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): Response<ChatResponse>
}