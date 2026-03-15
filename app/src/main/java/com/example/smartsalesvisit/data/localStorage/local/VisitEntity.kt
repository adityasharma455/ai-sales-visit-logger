package com.example.smartsalesvisit.data.localStorage.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visits")
data class VisitEntity(

    @PrimaryKey
    val id: String,

    val salesPersonId: String,

    val customerName: String,
    val contactPerson: String,

    val location: String,

    val visitDate: Long,

    val rawNotes: String,

    val meetingSummary: String?,
    val painPoints: String?,
    val actionItems: String?,
    val nextStep: String?,

    val outcomeStatus: String,

    val followUpDate: String?,

    val aiStatus: String,

    val syncStatus: String
)
