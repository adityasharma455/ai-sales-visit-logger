package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.repository.LocalStorageRepository
import kotlinx.coroutines.flow.Flow

class updateVisitUseCase(private val repo: LocalStorageRepository) {
    fun updateVisit(visit: Visit): Flow<ResultState<Boolean>> = repo.updateVisit(visit)
}