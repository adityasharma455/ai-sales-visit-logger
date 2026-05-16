package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.domain.repository.ServerRepository

class GetVisitsByDateForCurrentUserUseCase(private  val repo: ServerRepository) {
    fun getVisitsByDateForCurrentUser(start: Long, end: Long) =repo.getVisitsByDateForCurrentUser(start,end)
}