package com.example.smartsalesvisit.domain.repository

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.SalesPerson
import com.example.smartsalesvisit.domain.models.Visit
import kotlinx.coroutines.flow.Flow

interface ServerRepository {

    fun regisertSalePerson(salesPerson : SalesPerson): Flow<ResultState<Boolean>>
    fun logInSalePerson(email: String, password: String): Flow<ResultState<Boolean>>

    fun uploadVisit(visit: Visit) : Flow<ResultState<Boolean>>
}