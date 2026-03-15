package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.data.serverRepository.serverRepoImpl
import com.example.smartsalesvisit.domain.models.SalesPerson
import com.example.smartsalesvisit.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow

class registerSalePersonUseCase(private val repo: ServerRepository) {
    fun registerSalePerson(salesPerson: SalesPerson): Flow<ResultState<Boolean>> = repo.regisertSalePerson(salesPerson)
}