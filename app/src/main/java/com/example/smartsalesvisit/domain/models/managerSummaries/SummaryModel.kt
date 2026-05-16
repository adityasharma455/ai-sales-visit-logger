package com.example.smartsalesvisit.domain.models.managerSummaries

data class SummaryModel(
    val actions: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val insights: List<String> = emptyList(),
    val overallSummary: String = "",
    val territorySummaries: List<TerritorySummary> = emptyList(),
    val topTerritory: String = ""
)