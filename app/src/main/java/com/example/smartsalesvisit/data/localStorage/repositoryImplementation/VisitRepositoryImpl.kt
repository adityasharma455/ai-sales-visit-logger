package com.example.smartsalesvisit.data.localStorage.repositoryImplementation

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.data.localStorage.local.VisitDao
import com.example.smartsalesvisit.data.localStorage.local.VisitEntity
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.repository.LocalStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class VisitRepositoryImpl(
    private val dao: VisitDao
): LocalStorageRepository {
    override fun addVisit(visit: Visit): Flow<ResultState<Boolean>> = flow {

        emit(ResultState.Loading)

        try {

            dao.insertVisit(visit.toEntity())

            emit(ResultState.Success(true))

        } catch (e: Exception) {

            emit(ResultState.Error(e.message ?: "Failed to insert visit"))

        }
    }

    override fun getVisits(): Flow<ResultState<List<Visit>>> {

        return dao.getAllVisits().map { entityList ->

            try {

                val visits = entityList.map { it.toDomain() }

                ResultState.Success(visits)

            } catch (e: Exception) {

                ResultState.Error(e.message ?: "Error fetching visits")

            }

        }
    }

    override fun updateVisit(visit: Visit): Flow<ResultState<Boolean>> = flow {

        emit(ResultState.Loading)

        try {

            dao.updateVisit(visit.toEntity())

            emit(ResultState.Success(true))

        } catch (e: Exception) {

            emit(ResultState.Error(e.message ?: "Failed to update visit"))

        }

    }

    override suspend fun getVisitById(id: String): Flow<ResultState<Visit>> = flow {
        emit(ResultState.Loading)

         try {

            val visit = dao.getVisitById(id).toDomain()

            emit(ResultState.Success(visit))

        } catch (e: Exception) {

            emit(ResultState.Error(e.message ?: "Visit not found"))

        }

    }

    override suspend fun getUnsyncedVisits(): List<Visit> {

        return dao.getUnsyncedVisits().map {
            it.toDomain()
        }

    }
}

fun Visit.toEntity(): VisitEntity {
    return VisitEntity(
        id,
        salesPersonId,
        customerName,
        contactPerson,
        location,
        visitDate,
        rawNotes,
        meetingSummary,
        painPoints,
        actionItems,
        nextStep,
        outcomeStatus,
        followUpDate,
        aiStatus,
        syncStatus
    )
}

fun VisitEntity.toDomain(): Visit {
    return Visit(
        id,
        salesPersonId,
        customerName,
        contactPerson,
        location,
        visitDate,
        rawNotes,
        meetingSummary,
        painPoints,
        actionItems,
        nextStep,
        outcomeStatus,
        followUpDate,
        aiStatus,
        syncStatus
    )
}