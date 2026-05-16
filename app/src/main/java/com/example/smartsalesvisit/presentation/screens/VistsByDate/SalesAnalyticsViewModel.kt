package com.example.smartsalesvisit.presentation.screens.VistsByDate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.useCase.GetVisitsByDateForCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

class SalesAnalyticsViewModel(
    private val getVisitsByDateForCurrentUserUseCase : GetVisitsByDateForCurrentUserUseCase
) : ViewModel() {

    private val _getVisitsByDateForCurrentUserState = MutableStateFlow(GetVisitsByDateForCurrentUser())
    val getVisitsByDateForCurrentUserState = _getVisitsByDateForCurrentUserState.asStateFlow()

    fun fetchVisitsByDate(selectedDate: Long) {

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"))
        calendar.timeInMillis = selectedDate

        // 🔥 START OF DAY
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis

        // 🔥 END OF DAY
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val end = calendar.timeInMillis

        viewModelScope.launch {
            getVisitsByDateForCurrentUserUseCase.getVisitsByDateForCurrentUser(start, end).collect {
                when(it){
                    is ResultState.Loading -> {
                        _getVisitsByDateForCurrentUserState.value = GetVisitsByDateForCurrentUser(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getVisitsByDateForCurrentUserState.value = GetVisitsByDateForCurrentUser(Success = it.data, isLoading = false)
                    }
                    is ResultState.Error -> {
                        _getVisitsByDateForCurrentUserState.value = GetVisitsByDateForCurrentUser(Error = it.message.toString(), isLoading = false)
                    }
                }
            }
        }
    }


}


data class GetVisitsByDateForCurrentUser(
    val isLoading: Boolean?= false,
    val Success: List<Visit> ? = emptyList<Visit>(),
    val Error: String?= null
)