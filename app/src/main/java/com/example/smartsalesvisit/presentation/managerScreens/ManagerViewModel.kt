package com.example.smartsalesvisit.presentation.managerScreens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.SalesPerson
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.domain.useCase.GetAllVisitsByTerritoryUseCase
import com.example.smartsalesvisit.domain.useCase.GetSalePersonByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManagerViewModel(
    private val getAllVisitsByTerritoryUseCase: GetAllVisitsByTerritoryUseCase,
    private val getSalePersonByIdUseCase: GetSalePersonByIdUseCase
) : ViewModel() {

    // 🔥 VISITS STATE (UNCHANGED)
    private val _getAllTerritoryVisitState = MutableStateFlow(GetAllVisitsByTerritory())
    val getAllTerritoryVisitState = _getAllTerritoryVisitState.asStateFlow()

    // 🔥 ✅ NEW: MAP STATE (IMPORTANT FIX)
    private val _salesPersonMap = MutableStateFlow<Map<String, SalesPerson>>(emptyMap())
    val salesPersonMap = _salesPersonMap.asStateFlow()

    fun getAllTerritoryVisit(territory: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getAllVisitsByTerritoryUseCase.getAllVisitsByTerritory(territory).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllTerritoryVisitState.value =
                            GetAllVisitsByTerritory(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getAllTerritoryVisitState.value =
                            GetAllVisitsByTerritory(Success = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _getAllTerritoryVisitState.value =
                            GetAllVisitsByTerritory(
                                Error = it.message ?: "Error",
                                isLoading = false
                            )
                    }
                }
            }
        }
    }

    // 🔥 ✅ FIXED FUNCTION (STORE BY ID)
    fun getSalesPersonById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {

            // ✅ avoid duplicate API calls
            if (_salesPersonMap.value.containsKey(id)) return@launch

            getSalePersonByIdUseCase.getSalePersonById(id).collect {
                when (it) {
                    is ResultState.Success -> {
                        it.data?.let { person ->
                            _salesPersonMap.value =
                                _salesPersonMap.value + (id to person)
                            Log.d("viewModel salePerosn info", "${it.data}")
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}

// ✅ KEEP THIS SAME
data class GetAllVisitsByTerritory(
    val isLoading: Boolean? = false,
    val Success: List<Visit> = emptyList(),
    val Error: String? = null
)