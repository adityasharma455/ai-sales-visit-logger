package com.example.smartsalesvisit.domain.repository


import com.example.smartsalesvisit.common.ResultState
import kotlinx.coroutines.flow.Flow

interface ChatBotRepository {
    fun sendMessage(message: String): Flow<ResultState<String>>
}