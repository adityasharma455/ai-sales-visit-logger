package com.example.smartsalesvisit.common

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.repository.LocalStorageRepository
import com.example.smartsalesvisit.domain.repository.ServerRepository
import com.example.smartsalesvisit.domain.useCase.AiVisitUseCase
import com.example.smartsalesvisit.domain.useCase.TanscribeAudioUserUseCase
import java.io.File

class SyncManager(
    private val visitRepository: LocalStorageRepository,
    private val serverRepository: ServerRepository,
    private val aiVisitUseCase: AiVisitUseCase,
    private val transcribeUseCase: TanscribeAudioUserUseCase
) {

    suspend fun syncVisits() {

        val unsyncedVisits = visitRepository.getUnsyncedVisits()

        unsyncedVisits.forEach { visit ->

            try {

                var currentVisit = visit

                println("🔄 SYNC START: ${visit.id}")

                // 🔥 STEP 1: SPEECH TO TEXT (ONLY IF NOTES EMPTY)
                if (currentVisit.rawNotes.isNullOrBlank() &&
                    !currentVisit.audioFilePath.isNullOrBlank()
                ) {

                    println("🎤 STT START")

                    transcribeUseCase.transcribeAudio(currentVisit.audioFilePath!!).collect { result ->

                        if (result is ResultState.Success) {

                            currentVisit = currentVisit.copy(
                                rawNotes = result.data
                            )

                            visitRepository.updateVisit(currentVisit).collect {}

                            println("✅ STT DONE")
                        }
                    }
                }

                // 🔥 STEP 2: AI PROCESSING
                if (currentVisit.aiStatus != "DONE") {

                    println("🤖 AI START")

                    aiVisitUseCase.generateVisitAi(currentVisit).collect { result ->

                        if (result is ResultState.Success) {

                            currentVisit = result.data.copy(
                                aiStatus = "DONE"
                            )

                            visitRepository.updateVisit(currentVisit).collect {}

                            println("✅ AI DONE")
                        }
                    }
                }

                // 🔥 STEP 3: UPLOAD + CLEANUP (ONLY IF AI DONE)
                if (currentVisit.aiStatus == "DONE" &&
                    currentVisit.syncStatus != "SYNCED"
                ) {

                    println("☁️ UPLOAD START")

                    serverRepository.uploadVisit(currentVisit).collect { result ->

                        if (result is ResultState.Success) {

                            println("✅ UPLOAD DONE")

                            // 🔥 DELETE FILE SAFELY
                            val file = currentVisit.audioFilePath?.let { File(it) }

                            if (file != null && file.exists()) {
                                val deleted = file.delete()
                                println("🗑 FILE DELETE STATUS: $deleted | PATH: ${file.path}")
                            }

                            // 🔥 UPDATE FINAL STATE
                            val syncedVisit = currentVisit.copy(
                                syncStatus = "SYNCED",
                                audioFilePath = null // 🔥 VERY IMPORTANT
                            )

                            visitRepository.updateVisit(syncedVisit).collect {}

                            println("🎉 SYNC COMPLETE: ${visit.id}")
                        }
                    }
                }

            } catch (e: Exception) {

                println("❌ SYNC FAILED for ${visit.id}: ${e.message}")

                visitRepository.updateVisit(
                    visit.copy(syncStatus = "FAILED")
                ).collect {}
            }
        }
    }
}