package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.data.serverRepository.serverRepoImpl
import com.example.smartsalesvisit.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow

class LogInSalePersonUseCase(private val repo: ServerRepository) {
    fun logInSalePerson(email: String, password: String): Flow<ResultState<Boolean>> = repo.logInSalePerson(email, password)
}