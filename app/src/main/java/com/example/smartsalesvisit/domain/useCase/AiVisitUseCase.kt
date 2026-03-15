package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.repository.AiRepository
import kotlinx.coroutines.flow.Flow

class AiVisitUseCase(private val repo: AiRepository) {
    fun generateVisitAi(visit: Visit) : Flow<ResultState<Visit>> = repo.generateVisitAI(visit)
}