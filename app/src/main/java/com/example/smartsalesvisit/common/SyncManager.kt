package com.example.smartsalesvisit.common

import com.example.smartsalesvisit.domain.repository.LocalStorageRepository
import com.example.smartsalesvisit.domain.repository.ServerRepository
import com.example.smartsalesvisit.domain.useCase.AiVisitUseCase

class SyncManager(
    private val visitRepository: LocalStorageRepository,
    private val serverRepository: ServerRepository,
    private val aiVisitUseCase: AiVisitUseCase
) {
    suspend fun syncVisits() {
        val unsyncedVisits = visitRepository.getUnsyncedVisits()

        unsyncedVisits.forEach { visit ->
            try {
                var currentVisit = visit

                // 1️⃣ Step: AI Processing
                if (currentVisit.aiStatus != "DONE") {
                    aiVisitUseCase.generateVisitAi(currentVisit).collect { result ->
                        if (result is ResultState.Success) {
                            currentVisit = result.data.copy(aiStatus = "DONE")
                            // Save AI result immediately so we don't lose it if Upload fails
                            visitRepository.updateVisit(currentVisit).collect {}
                        }
                    }
                }

                // 2️⃣ Step: Upload to Firebase (Only if AI is done)
                if (currentVisit.aiStatus == "DONE") {
                    serverRepository.uploadVisit(currentVisit).collect { result ->
                        if (result is ResultState.Success) {
                            // 3️⃣ Step: Final Sync Mark
                            visitRepository.updateVisit(
                                currentVisit.copy(syncStatus = "SYNCED")
                            ).collect {}
                        }
                    }
                }
            } catch (e: Exception) {
                println("SYNC FAILED for ${visit.id}: ${e.message}")
                // Optional: Mark as failed in DB to show error icon in UI
                visitRepository.updateVisit(visit.copy(syncStatus = "FAILED")).collect {}
            }
        }
    }
}