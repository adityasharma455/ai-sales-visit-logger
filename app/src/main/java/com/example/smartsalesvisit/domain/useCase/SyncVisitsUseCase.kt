package com.example.smartsalesvisit.domain.useCase

import com.example.smartsalesvisit.common.SyncManager

class SyncVisitsUseCase(private val syncManager: SyncManager) {
    suspend operator fun invoke() {
        syncManager.syncVisits()
    }
}