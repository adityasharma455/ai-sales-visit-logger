package com.example.smartsalesvisit.data.localStorage.searchRepository


import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.data.AI.network.SarvamApiBuilder
import com.example.smartsalesvisit.domain.repository.searchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SarvamRepositoryImplementation : searchRepository {

    override fun transcribeAudio(audioPath: String): Flow<ResultState<String>> = flow {

        emit(ResultState.Loading)

        try {

            val file = File(audioPath)

            // 🔥 DEBUG FILE
            println("FILE PATH: ${file.absolutePath}")
            println("FILE EXISTS: ${file.exists()}")
            println("FILE SIZE: ${file.length()}")

            if (!file.exists() || file.length() == 0L) {
                emit(ResultState.Error("Audio file missing or empty"))
                return@flow
            }

            val requestFile = RequestBody.create(
                "application/octet-stream".toMediaTypeOrNull(),
                file
            )

            val body = MultipartBody.Part.createFormData(
                "file",   // 🔥 MUST MATCH CURL
                file.name,
                requestFile
            )

            println("SENDING REQUEST TO SARVAM...")

            val response = SarvamApiBuilder.api.transcribeAudio(body)

            // 🔥 DEBUG RESPONSE
            println("RESPONSE CODE: ${response.code()}")

            if (response.isSuccessful) {

                val transcript = response.body()?.transcript ?: ""

                println("TRANSCRIPT: $transcript")

                emit(ResultState.Success(transcript))

            } else {

                val errorBody = response.errorBody()?.string()

                println("ERROR BODY: $errorBody")

                emit(ResultState.Error("API ERROR: $errorBody"))
            }

        } catch (e: Exception) {

            println("EXCEPTION: ${e.message}")

            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }
}