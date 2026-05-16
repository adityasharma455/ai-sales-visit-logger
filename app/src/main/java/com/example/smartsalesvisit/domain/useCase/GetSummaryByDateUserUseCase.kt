package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.domain.repository.ServerRepository

class GetSummaryByDateUserUseCase(private val repo: ServerRepository) {
    fun getSummaryByDateUserUseCase(selectedDate: Long) = repo.getSummaryByDate(selectedDate)
}