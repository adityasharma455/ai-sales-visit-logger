package com.example.smartsalesvisit.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Visit(

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
    val customerEmotion: String? = null,
    val dealProbability: String? = null,
    val suggestedStrategy: String? = null,

    val outcomeStatus: String,

    val followUpDate: String?,

    val aiStatus: String,

    val syncStatus: String
) : Parcelable
