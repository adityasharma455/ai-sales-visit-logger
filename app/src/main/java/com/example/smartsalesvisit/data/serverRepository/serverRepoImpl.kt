package com.example.smartsalesvisit.data.serverRepository

import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.common.SALE_PERSONS
import com.example.smartsalesvisit.domain.models.SalesPerson
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.repository.ServerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class serverRepoImpl(
    private val auth: FirebaseAuth,
    private val firebase: FirebaseFirestore
): ServerRepository {
    override fun regisertSalePerson(salesPerson: SalesPerson): Flow<ResultState<Boolean>> = flow{
            emit(ResultState.Loading)
        try {
            val result = auth.createUserWithEmailAndPassword(salesPerson.email, salesPerson.password).await()
            val uid = result.user?.uid ?: throw Exception("Registration failed")

            val saledPersonData = salesPerson.copy(id = uid)
            firebase.collection(SALE_PERSONS).document(uid).set(saledPersonData).await()
            emit(ResultState.Success(true))


        }catch (e: Exception){
            emit(ResultState.Error(e.message.toString()))
        }

    }

    override fun logInSalePerson(
        email: String,
        password: String
    ): Flow<ResultState<Boolean>> = flow{
        emit(ResultState.Loading)
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("LogIn Failed")

            val snapshot = firebase.collection(SALE_PERSONS).document(uid)
                .get().await()
            val salePerson = snapshot.toObject<SalesPerson>(SalesPerson::class.java)
                ?: throw Exception("Sale Person profile is not found")

            emit(ResultState.Success(true))


        }catch (e: Exception){
            emit(ResultState.Error(e.message.toString()))
        }

    }

    override fun uploadVisit(visit: Visit): Flow<ResultState<Boolean>> = flow {

        emit(ResultState.Loading)

        try {

            val uid = auth.currentUser?.uid
                ?: throw Exception("User not logged in")

            val visitMap = hashMapOf(
                "id" to visit.id,
                "customerName" to visit.customerName,
                "contactPerson" to visit.contactPerson,
                "location" to visit.location,
                "rawNotes" to visit.rawNotes,
                "meetingSummary" to visit.meetingSummary,
                "painPoints" to visit.painPoints,
                "actionItems" to visit.actionItems,
                "nextStep" to visit.nextStep,
                "outcomeStatus" to visit.outcomeStatus,
                "visitDate" to visit.visitDate,
                "salesPersonId" to visit.salesPersonId,
                "followUpDate" to visit.followUpDate
            )


            firebase.collection(SALE_PERSONS)
                .document(uid)
                .collection("visits")
                .document(visit.id)
                .set(visitMap)
                .await()

            emit(ResultState.Success(true))

        } catch (e: Exception) {

            emit(ResultState.Error(e.message.toString() ?: "Upload Failed"))

        }

    }

}