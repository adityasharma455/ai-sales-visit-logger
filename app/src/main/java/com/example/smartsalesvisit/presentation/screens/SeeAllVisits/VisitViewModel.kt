package com.example.smartsalesvisit.presentation.screens.SeeAllVisits

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.common.SyncScheduler
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.useCase.SyncVisitsUseCase
import com.example.smartsalesvisit.domain.useCase.getVisitsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VisitViewModel(
    private val getVisitUseCase: getVisitsUseCase,
): ViewModel(){

    private val _visitState = MutableStateFlow(GetAllVisitsState())
    val visitState = _visitState.asStateFlow()


    fun getAllVisits(){
        viewModelScope.launch {
            getVisitUseCase.getVisits().collect {
                when(it) {
                    is ResultState.Loading ->{
                        _visitState.value = GetAllVisitsState(isLoading = true)
                    }
                    is ResultState.Success ->{
                        _visitState.value = GetAllVisitsState(Success = it.data, isLoading = false)
                    }
                    is ResultState.Error ->{
                        _visitState.value = GetAllVisitsState(Error = it.message.toString(), isLoading = false)
                    }

                }
            }
        }
    }


    fun refreshVisits(context: Context) {

        viewModelScope.launch {

            _visitState.value = _visitState.value.copy(isRefreshing = true)

            SyncScheduler.startSync(context)

            kotlinx.coroutines.delay(1200)


            _visitState.value = _visitState.value.copy(isRefreshing = false)

        }

    }

}

data class GetAllVisitsState(
    val isLoading: Boolean? =false,
    val isRefreshing: Boolean = false,
    val Success: List<Visit>  = emptyList<Visit>(),
    val Error: String?=null
)