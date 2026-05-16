package com.example.smartsalesvisit.data.serverRepository

import android.util.Log
import com.example.smartsalesvisit.common.MANAGERS
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.common.SALE_PERSONS
import com.example.smartsalesvisit.domain.models.ManagerModel
import com.example.smartsalesvisit.domain.models.SalesPerson
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.models.managerSummaries.SummaryModel
import com.example.smartsalesvisit.domain.models.managerSummaries.TerritorySummary
import com.example.smartsalesvisit.domain.repository.ServerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

            var currentUser = auth.currentUser?.uid ?: "Local user"

            val uid = auth.currentUser?.uid
                ?: throw Exception("User not logged in")

            val visitMap = hashMapOf(
                "id" to visit.id,
                "customerName" to visit.customerName,
                "contactPerson" to visit.contactPerson,
                "customerEmail" to visit.customerEmail,
                "location" to visit.location,
                "rawNotes" to visit.rawNotes,
                "audioFilePath" to visit.audioFilePath,

                // ✅ FIXED (removed wrong key)
                "visitDate" to visit.visitDate,

                // ✅ IMPORTANT (normalize)
                "territory" to visit.territory.lowercase(),

                "meetingSummary" to visit.meetingSummary,
                "painPoints" to visit.painPoints,
                "actionItems" to visit.actionItems,
                "nextStep" to visit.nextStep,
                "customerEmotion" to visit.customerEmotion,
                "dealProbability" to visit.dealProbability,
                "suggestedStrategy" to visit.suggestedStrategy,
                "outcomeStatus" to visit.outcomeStatus,

                // 🔥 CRITICAL FOR FILTERING
                "salesPersonId" to currentUser,

                "followUpDate" to visit.followUpDate,

                // ✅ ADD THESE (avoid crash)
                "aiStatus" to visit.aiStatus,
                "syncStatus" to visit.syncStatus
            )

            // ✅ 1. Salesperson-specific (for offline/fast access)
            firebase.collection(SALE_PERSONS)
                .document(uid)
                .collection("visits")
                .document(visit.id)
                .set(visitMap)
                .await()

            // ✅ 2. GLOBAL collection (for manager queries)
            firebase.collection("visits")
                .document(visit.id)
                .set(visitMap)
                .await()

            emit(ResultState.Success(true))

        } catch (e: Exception) {

            emit(ResultState.Error(e.message ?: "Upload Failed"))

        }

    }

    override fun registerManager(managerModel: ManagerModel): Flow<ResultState<Boolean>> = flow {
        emit(ResultState.Loading)
        try {
            val result = auth.createUserWithEmailAndPassword(managerModel.email, managerModel.password).await()
            val uid = result.user?.uid ?: throw Exception("Registration failed")

            val saledPersonData =  managerModel.copy(id = uid)
            firebase.collection(SALE_PERSONS).document(uid).set(saledPersonData).await()
            emit(ResultState.Success(true))


        }catch (e: Exception){
            emit(ResultState.Error(e.message.toString() ?: "Registration failed"))
        }
    }

    override fun logInManager(
        email: String,
        password: String
    ): Flow<ResultState<Boolean>> =flow{
        emit(ResultState.Loading)
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("LogIn Failed")

            val snapshot = firebase.collection(MANAGERS).document(uid)
                .get().await()
            emit(ResultState.Success(true))


        }catch (e: Exception){
            emit(ResultState.Error(e.message.toString() ?: "Error Occured"))
        }


    }

    override fun getAllTerritorySalePersonVisits(
        territory: String
    ): Flow<ResultState<List<Visit>>> = flow {

        emit(ResultState.Loading)

        try {

            val snapshot = firebase.collection("visits")
                .whereEqualTo("territory", territory.trim().lowercase())
                .get()
                .await()

            val visits = snapshot.documents.mapNotNull {
                it.toObject(Visit::class.java)
            }


            emit(ResultState.Success(visits))

        } catch (e: Exception) {

            emit(ResultState.Error(e.message ?: "Error fetching visits"))

        }

    }

    override fun getSalePersonById(id: String): Flow<ResultState<SalesPerson>> = flow {
        emit(ResultState.Loading)
        try {
            val snapshot = firebase.collection(SALE_PERSONS)
                .document(id)
                .get()
                .await()

            val salesPerson = snapshot.toObject(SalesPerson::class.java)
                ?: throw Exception("SalesPerson not found")

            emit(ResultState.Success(salesPerson))
            Log.d("SalePerson repo info ", "${salesPerson}")

        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error"))
        }
    }

    override fun getVisitsByDateForCurrentUser(
        start: Long,
        end: Long
    ): Flow<ResultState<List<Visit>>> = flow {

        emit(ResultState.Loading)

        try {

            val uid = auth.currentUser?.uid
                ?: throw Exception("User not logged in")

            val snapshot = firebase.collection("visits")
                .whereEqualTo("salesPersonId", uid)
                .whereGreaterThanOrEqualTo("visitDate", start)
                .whereLessThanOrEqualTo("visitDate", end)
                .get()
                .await()

            val visits = snapshot.documents.mapNotNull {
                it.toObject(Visit::class.java)
            }

            emit(ResultState.Success(visits))

        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error fetching visits"))
        }
    }

    override fun getSummaryByDate(
        selectedDate: Long
    ): Flow<ResultState<SummaryModel>> = flow {

        emit(ResultState.Loading)

        try {

            val snapshot = firebase.collection("summaries")
                .get()
                .await()

            if (snapshot.isEmpty) {
                emit(ResultState.Error("No summaries found"))
                return@flow
            }

            val targetDate = getDateOnly(selectedDate)

            val matchedDoc = snapshot.documents.firstOrNull { doc ->

                val docIdMillis = doc.id.toLongOrNull() ?: return@firstOrNull false

                val docDate = getDateOnly(docIdMillis)

                docDate == targetDate
            }

            if (matchedDoc == null) {
                emit(ResultState.Error("No summary found for selected date"))
                return@flow
            }

            val summary = SummaryModel(
                actions = matchedDoc.get("actions") as? List<String> ?: emptyList(),
                createdAt = matchedDoc.id.toLongOrNull() ?: 0L,
                insights = matchedDoc.get("insights") as? List<String> ?: emptyList(),
                overallSummary = matchedDoc.getString("overallSummary") ?: "",
                territorySummaries = (matchedDoc.get("territorySummaries") as? List<Map<String, Any>>)
                    ?.map {
                        TerritorySummary(
                            avgDealProbability = (it["avgDealProbability"] as? Number)?.toDouble() ?: 0.0,
                            performance = it["performance"] as? String ?: "",
                            rank = (it["rank"] as? Number)?.toInt() ?: 0,
                            summary = it["summary"] as? String ?: "",
                            territory = it["territory"] as? String ?: "",
                            totalVisits = (it["totalVisits"] as? Number)?.toInt() ?: 0
                        )
                    } ?: emptyList(),
                topTerritory = matchedDoc.getString("topTerritory") ?: ""
            )

            emit(ResultState.Success(summary))

        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error fetching summary"))
        }
    }







}

fun getDateOnly(millis: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    sdf.timeZone = java.util.TimeZone.getTimeZone("Asia/Kolkata")
    return sdf.format(java.util.Date(millis))
}
