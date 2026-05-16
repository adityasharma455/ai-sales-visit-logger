package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.domain.repository.ChatBotRepository

class ChatBotUseCase(private val repo: ChatBotRepository) {
    fun chatBotUseCase(message: String) = repo.sendMessage(message)
}