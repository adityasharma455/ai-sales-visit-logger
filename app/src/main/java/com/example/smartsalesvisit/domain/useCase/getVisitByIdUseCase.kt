package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.repository.LocalStorageRepository
import kotlinx.coroutines.flow.Flow

class getVisitByIdUseCase(private val repo: LocalStorageRepository) {
    suspend fun getVisitById(id: String) : Flow<ResultState<Visit>> = repo.getVisitById(id)
}