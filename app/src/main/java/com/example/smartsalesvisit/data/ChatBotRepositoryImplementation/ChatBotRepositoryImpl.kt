package com.example.smartsalesvisit.data.ChatBotRepositoryImplementation


import android.util.Log
import androidx.compose.remote.creation.log
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.data.AI.network.ChatBotApiBuilder
import com.example.smartsalesvisit.domain.models.chatBot.ChatRequest
import com.example.smartsalesvisit.domain.repository.ChatBotRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatBotRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : ChatBotRepository {

    override fun sendMessage(message: String): Flow<ResultState<String>> = flow {

        emit(ResultState.Loading)

        try {

            val userId = firebaseAuth.currentUser?.uid ?: "guest_user"

            val response = ChatBotApiBuilder.api.sendMessage(
                ChatRequest(
                    user_id = userId,
                    message = message
                )
            )
            println("CHATBOT REQUEST: $message")


            println("CHATBOT RESPONSE CODE: ${response.code()}")

            if (response.isSuccessful) {

                val body = response.body()

                Log.d("CHATBOT_FULL_RESPONSE", "$body")

                val reply = body?.reply

                if (reply.isNullOrBlank()) {
                    Log.e("CHATBOT", "Reply is null or empty")
                    emit(ResultState.Error("Empty reply from server"))
                } else {
                    Log.d("CHATBOT", "Reply: $reply")
                    emit(ResultState.Success(reply))
                }

            } else {
                val error = response.errorBody()?.string()
                Log.e("CHATBOT_ERROR", "$error")
                emit(ResultState.Error("API ERROR: $error"))
            }

        } catch (e: Exception) {

            println("CHATBOT EXCEPTION: ${e.message}")

            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }
}