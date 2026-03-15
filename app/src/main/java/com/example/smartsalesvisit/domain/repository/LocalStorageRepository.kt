package com.example.smartsalesvisit.domain.repository

import androidx.room.Dao
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.data.localStorage.local.VisitDao
import com.example.smartsalesvisit.data.localStorage.local.VisitEntity
import com.example.smartsalesvisit.domain.models.Visit
import kotlinx.coroutines.flow.Flow

interface LocalStorageRepository {

    fun addVisit(visit: Visit) : Flow<ResultState<Boolean>>
    fun getVisits(): Flow<ResultState<List<Visit>>>
    fun updateVisit(visit: Visit) : Flow<ResultState<Boolean>>
     suspend fun getVisitById(id: String): Flow<ResultState<Visit>>

    suspend fun getUnsyncedVisits(): List<Visit>

}