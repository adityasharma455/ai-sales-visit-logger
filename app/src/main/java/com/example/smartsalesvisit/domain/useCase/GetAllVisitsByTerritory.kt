package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.domain.repository.ServerRepository

class GetAllVisitsByTerritoryUseCase(private val repo: ServerRepository) {
    fun getAllVisitsByTerritory(territory: String) = repo.getAllTerritorySalePersonVisits(territory)
}