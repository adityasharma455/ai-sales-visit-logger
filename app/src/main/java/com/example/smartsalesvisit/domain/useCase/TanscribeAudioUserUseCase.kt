package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.domain.repository.searchRepository

class TanscribeAudioUserUseCase(private val repo : searchRepository) {
    fun transcribeAudio(audioPath: String)  =repo.transcribeAudio(audioPath)
}