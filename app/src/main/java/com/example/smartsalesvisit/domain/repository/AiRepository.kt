package com.example.smartsalesvisit.domain.repository

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.Visit
import kotlinx.coroutines.flow.Flow

interface AiRepository {
    fun generateVisitAI(
        visit: Visit
    ): Flow<ResultState<Visit>>
}