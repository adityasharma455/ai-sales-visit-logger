package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.repository.LocalStorageRepository
import kotlinx.coroutines.flow.Flow


class addVisitUseCase(private val repo: LocalStorageRepository ) {
    fun addVisit(visit: Visit): Flow<ResultState<Boolean>> = repo.addVisit(visit)

}