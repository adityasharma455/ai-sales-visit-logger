package com.example.smartsalesvisit.domain.models

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Visit(

    val id: String = "",
    val salesPersonId: String = "",

    val customerName: String = "",
    val contactPerson: String = "",
    val customerEmail: String = "",
    val location: String = "",
    val visitDate: Long = 0L,
    val territory: String = "",
    val rawNotes: String = "",
    val audioFilePath: String? = null,

    val meetingSummary: String? = null,
    val painPoints: String? = null,
    val actionItems: String? = null,
    val nextStep: String? = null,
    val customerEmotion: String? = null,
    val dealProbability: String? = null,
    val suggestedStrategy: String? = null,

    val outcomeStatus: String = "",

    val followUpDate: String? = null,

    val aiStatus: String = "",

    val syncStatus: String = ""

) : Parcelable