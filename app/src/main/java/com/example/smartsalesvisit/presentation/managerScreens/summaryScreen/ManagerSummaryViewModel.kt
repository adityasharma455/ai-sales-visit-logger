package com.example.smartsalesvisit.presentation.managerScreens.summaryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.managerSummaries.SummaryModel
import com.example.smartsalesvisit.domain.useCase.GetSummaryByDateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

class ManagerSummaryViewModel(
    private val getSummaryByDateUserUseCase: GetSummaryByDateUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SummaryState())
    val state = _state.asStateFlow()

    fun fetchSummaryByDate(selectedDate: Long) {
        viewModelScope.launch {
            getSummaryByDateUserUseCase.getSummaryByDateUserUseCase(selectedDate)
                .collect {
                    when (it) {
                        is ResultState.Loading -> {
                            _state.value = SummaryState(isLoading = true)
                        }
                        is ResultState.Success -> {
                            _state.value = SummaryState(summary = it.data)
                        }
                        is ResultState.Error -> {
                            _state.value = SummaryState(error = it.message)
                        }
                    }
                }
        }
    }

}

data class SummaryState(
    val isLoading: Boolean = false,
    val summary: SummaryModel? = null,
    val error: String? = null
)