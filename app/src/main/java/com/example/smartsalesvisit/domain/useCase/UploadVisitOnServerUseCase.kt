package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.repository.ServerRepository

class UploadVisitOnServerUseCase(private val repo: ServerRepository) {
    fun uploadVisit(visit: Visit) = repo.uploadVisit(visit)
}