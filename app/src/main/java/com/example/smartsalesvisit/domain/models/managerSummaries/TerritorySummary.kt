package com.example.smartsalesvisit.domain.models.managerSummaries

data class TerritorySummary(
    val avgDealProbability: Double = 0.0,
    val performance: String = "",
    val rank: Int = 0,
    val summary: String = "",
    val territory: String = "",
    val totalVisits: Int = 0
)