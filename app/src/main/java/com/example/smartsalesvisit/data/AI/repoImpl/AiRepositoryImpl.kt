package com.example.smartsalesvisit.data.AI.repoImpl


import com.example.smartsalesvisit.common.AiPrompts
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.data.AI.network.GeminiApiBuilder
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.models.geminiResponse.Content
import com.example.smartsalesvisit.domain.models.geminiResponse.GeminiRequest
import com.example.smartsalesvisit.domain.models.geminiResponse.Part
import com.example.smartsalesvisit.domain.repository.AiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout

class AiRepositoryImpl(
) : AiRepository {

    override fun generateVisitAI(
        visit: Visit
    ): Flow<ResultState<Visit>> = flow {

        emit(ResultState.Loading)

        try {
            // Timeout so it doesn't run forever
            val response = withTimeout(30_000) {
                GeminiApiBuilder.api.generateVisitSummary(
                    request = GeminiRequest(
                        contents = listOf(
                            Content(
                                parts = listOf(
                                    Part( text = AiPrompts.visitSummaryPrompt(visit.rawNotes))
                                    )
                                )
                            )
                        )
                    )

            }

            // Extract AI response text safely
            val aiText = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text ?: throw Exception("Empty AI response")

            // Parse fields
            val summary = Regex("Meeting Summary:\\s*(.*)")
                .find(aiText)?.groupValues?.get(1)?.trim()

            val painPoints = Regex("Pain Points:\\s*(.*)")
                .find(aiText)?.groupValues?.get(1)?.trim()

            val actionItems = Regex("Action Items:\\s*(.*)")
                .find(aiText)?.groupValues?.get(1)?.trim()

            val nextStep = Regex("Recommended Next Step:\\s*(.*)")
                .find(aiText)?.groupValues?.get(1)?.trim()

            val updatedVisit = visit.copy(
                meetingSummary = summary,
                painPoints = painPoints,
                actionItems = actionItems,
                nextStep = nextStep,
                aiStatus = "Completed"
            )

            emit(ResultState.Success(updatedVisit))

        } catch (e: Exception) {
            e.printStackTrace()
            // Mark AI as failed
            val failedVisit = visit.copy(aiStatus = "FAILED")
            emit(ResultState.Error(e.message ?: "AI generation failed"))
        }

    }
}