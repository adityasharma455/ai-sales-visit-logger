package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.domain.repository.ServerRepository

class GetSalePersonByIdUseCase(private val repo: ServerRepository) {
    fun getSalePersonById(id:String) = repo.getSalePersonById(id)
}