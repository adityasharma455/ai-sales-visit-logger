package com.example.smartsalesvisit.presentation.screens.AddVisitScreen

import android.R
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.common.NetworkUtils.NetworkUtils
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.useCase.AiVisitUseCase
import com.example.smartsalesvisit.domain.useCase.TanscribeAudioUserUseCase
import com.example.smartsalesvisit.domain.useCase.addVisitUseCase
import com.example.smartsalesvisit.domain.useCase.updateVisitUseCase
import com.example.smartsalesvisit.domain.useCase.UploadVisitOnServerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterVisitViewModel(
    private val addVisitUseCase: addVisitUseCase,
    private val updateVisitUseCase: updateVisitUseCase,
    private val aiVisitUseCase: AiVisitUseCase,
    private val uploadVisitOnServerUseCase: UploadVisitOnServerUseCase,
    private val tanscribeAudioUserUseCase : TanscribeAudioUserUseCase,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterVisitState())
    val state = _state.asStateFlow()

    private val _speechToTextState = MutableStateFlow(SpeechToText())
    val speechToTextState = _speechToTextState.asStateFlow()

    fun addVisit(visit: Visit) {

        viewModelScope.launch {

            val hasInternet = NetworkUtils.isInternetAvailable(context = context)

            if (!hasInternet) {

                // OFFLINE FLOW
                val offlineVisit = visit.copy(
                    aiStatus = "PENDING",
                    syncStatus = "PENDING"
                )

                addVisitUseCase.addVisit(offlineVisit).collect { result ->

                    when (result) {

                        is ResultState.Loading ->
                            _state.value = RegisterVisitState(isLoading = true)

                        is ResultState.Success ->
                            _state.value = RegisterVisitState(
                                Success = true,
                                isLoading = false
                            )

                        is ResultState.Error ->
                            _state.value = RegisterVisitState(
                                error = result.message
                            )
                    }
                }

            } else {

                processOnlineVisit(visit)

            }

        }

    }

    private suspend fun processOnlineVisit(visit: Visit) {

        _state.value = RegisterVisitState(isLoading = true)

        // 1️ SAVE INITIAL VISIT
        addVisitUseCase.addVisit(visit).collect {}

        // 2️ CALL AI
        aiVisitUseCase.generateVisitAi(visit).collect { aiResult ->

            when (aiResult) {

                is ResultState.Success -> {

                    // AI finished
                    val aiVisit = aiResult.data.copy(
                        aiStatus = "DONE"
                    )

                    // 3️ Upload to server
                    uploadVisitOnServerUseCase.uploadVisit(aiVisit).collect { serverResult ->

                        when (serverResult) {

                            is ResultState.Success -> {

                                // 4️ Mark synced locally
                                val syncedVisit = aiVisit.copy(
                                    syncStatus = "SYNCED"
                                )

                                updateVisitUseCase.updateVisit(syncedVisit).collect {}

                                _state.value = RegisterVisitState(
                                    Success = true,
                                    isLoading = false
                                )
                            }

                            is ResultState.Error -> {

                                // Upload failed → keep pending
                                val pendingVisit = aiVisit.copy(
                                    syncStatus = "PENDING"
                                )

                                updateVisitUseCase.updateVisit(pendingVisit).collect {}

                                _state.value = RegisterVisitState(
                                    Success = true,
                                    isLoading = false
                                )
                            }

                            else -> {}

                        }

                    }

                }

                is ResultState.Error -> {

                    // AI failed but visit should still be stored
                    val failedVisit = visit.copy(
                        aiStatus = "FAILED",
                        syncStatus = "PENDING"
                    )

                    updateVisitUseCase.updateVisit(failedVisit).collect {}

                    _state.value = RegisterVisitState(
                        Success = true,
                        isLoading = false
                    )
                }

                else -> {}

            }

        }

    }

    fun transcribeAudioToNotes(audioPath: String) {

        viewModelScope.launch {

            println("VM: Transcription started")
            println("VM: Audio Path = $audioPath")

            tanscribeAudioUserUseCase.transcribeAudio(audioPath).collect { result ->

                when (result) {

                    is ResultState.Loading -> {
                        println("VM: Loading...")
                        _speechToTextState.value = SpeechToText(isLoading = true)
                    }

                    is ResultState.Success -> {
                        println("VM: SUCCESS -> ${result.data}")
                        _speechToTextState.value = SpeechToText(
                            isLoading = false,
                            Success = result.data
                        )
                    }

                    is ResultState.Error -> {
                        println("VM: ERROR -> ${result.message}")
                        _speechToTextState.value = SpeechToText(
                            isLoading = false,
                            Error = result.message
                        )
                    }
                }
            }
        }
    }
}

data class RegisterVisitState(
    val isLoading: Boolean = false,
    val Success: Boolean? = null,
    val error: String? = ""
)

data class SpeechToText(
    val isLoading: Boolean = false,
    val Success: String? = null,
    val Error: String ? = ""
)