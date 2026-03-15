package com.example.smartsalesvisit.presentation.screens.UpdateVisits

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.common.NetworkUtils.NetworkUtils
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.useCase.AiVisitUseCase
import com.example.smartsalesvisit.domain.useCase.updateVisitUseCase
import com.example.smartsalesvisit.domain.useCase.UploadVisitOnServerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateVisitsViewModel(
    private val updateVisitUseCase: updateVisitUseCase,
    private val aiVisitUseCase: AiVisitUseCase,
    private val uploadVisitOnServerUseCase: UploadVisitOnServerUseCase,
    private val context: Context
) : ViewModel() {

    private val _updateVisitsState = MutableStateFlow(UpdateVisitsState())
    val updateVisitsState = _updateVisitsState.asStateFlow()

    fun updateVisits(visit: Visit) {

        viewModelScope.launch {

            val hasInternet = NetworkUtils.isInternetAvailable(context)

            if (!hasInternet) {

                // OFFLINE UPDATE
                val offlineVisit = visit.copy(
                    aiStatus = "PENDING",
                    syncStatus = "PENDING"
                )

                updateVisitUseCase.updateVisit(offlineVisit).collect { result ->

                    when (result) {

                        is ResultState.Loading ->
                            _updateVisitsState.value =
                                UpdateVisitsState(isLoading = true)

                        is ResultState.Success ->
                            _updateVisitsState.value =
                                UpdateVisitsState(Success = true, isLoading = false)

                        is ResultState.Error ->
                            _updateVisitsState.value =
                                UpdateVisitsState(Error = result.message, isLoading = false)

                    }

                }

            } else {

                processOnlineUpdate(visit)

            }

        }

    }

    private suspend fun processOnlineUpdate(visit: Visit) {

        _updateVisitsState.value = UpdateVisitsState(isLoading = true)

        // 1️ UPDATE ROOM FIRST
        updateVisitUseCase.updateVisit(visit).collect {}

        // 2️ CALL AI AGAIN (if notes changed)
        aiVisitUseCase.generateVisitAi(visit).collect { aiResult ->

            when (aiResult) {

                is ResultState.Success -> {

                    val aiVisit = aiResult.data.copy(
                        aiStatus = "DONE"
                    )

                    // 3️ UPDATE ROOM WITH AI DATA
                    updateVisitUseCase.updateVisit(aiVisit).collect {}

                    // 4️ UPLOAD TO SERVER
                    uploadVisitOnServerUseCase.uploadVisit(aiVisit).collect { serverResult ->

                        when (serverResult) {

                            is ResultState.Success -> {

                                val syncedVisit = aiVisit.copy(
                                    syncStatus = "SYNCED"
                                )

                                updateVisitUseCase.updateVisit(syncedVisit).collect {}

                                _updateVisitsState.value =
                                    UpdateVisitsState(Success = true, isLoading = false)

                            }

                            is ResultState.Error -> {

                                val pendingVisit = aiVisit.copy(
                                    syncStatus = "PENDING"
                                )

                                updateVisitUseCase.updateVisit(pendingVisit).collect {}

                                _updateVisitsState.value =
                                    UpdateVisitsState(Success = true, isLoading = false)

                            }

                            else -> {}

                        }

                    }

                }

                is ResultState.Error -> {

                    val failedVisit = visit.copy(
                        aiStatus = "FAILED",
                        syncStatus = "PENDING"
                    )

                    updateVisitUseCase.updateVisit(failedVisit).collect {}

                    _updateVisitsState.value =
                        UpdateVisitsState(Success = true, isLoading = false)

                }

                else -> {}

            }

        }

    }

}

data class UpdateVisitsState(
    val isLoading: Boolean = false,
    val Success: Boolean? = null,
    val Error: String? = null
)