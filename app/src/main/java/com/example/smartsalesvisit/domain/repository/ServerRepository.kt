package com.example.smartsalesvisit.domain.repository

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.ManagerModel
import com.example.smartsalesvisit.domain.models.SalesPerson
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.models.managerSummaries.SummaryModel
import kotlinx.coroutines.flow.Flow

interface ServerRepository {

    fun regisertSalePerson(salesPerson : SalesPerson): Flow<ResultState<Boolean>>
    fun logInSalePerson(email: String, password: String): Flow<ResultState<Boolean>>

    fun uploadVisit(visit: Visit) : Flow<ResultState<Boolean>>

    fun registerManager(managerModel: ManagerModel) : Flow<ResultState<Boolean>>
    fun logInManager(email: String, password: String): Flow<ResultState<Boolean>>

    fun getAllTerritorySalePersonVisits(territory: String) : Flow<ResultState<List<Visit>>>

    fun getSalePersonById(id: String) : Flow<ResultState<SalesPerson>>

    fun getVisitsByDateForCurrentUser(
        start: Long,
        end: Long
    ): Flow<ResultState<List<Visit>>>

    fun getSummaryByDate(
        selectedDate: Long
    ): Flow<ResultState<SummaryModel>>

}