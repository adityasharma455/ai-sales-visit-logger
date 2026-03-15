package com.example.smartsalesvisit.presentation.screens.AuthScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.models.SalesPerson
import com.example.smartsalesvisit.domain.useCase.LogInSalePersonUseCase
import com.example.smartsalesvisit.domain.useCase.registerSalePersonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class authViewModel(
    private val logInSalePersonUseCase: LogInSalePersonUseCase,
    private val registerSalePersonUseCase: registerSalePersonUseCase
): ViewModel() {

    private val _LogInAuthState = MutableStateFlow(LogInAuthState())
    val logInAuthState = _LogInAuthState.asStateFlow()

    private val _signUpstate = MutableStateFlow(SignUpAuthState())
    val signUpState = _signUpstate.asStateFlow()

    fun registerSalesPerson(salesPerson: SalesPerson){
        viewModelScope.launch {
            registerSalePersonUseCase.registerSalePerson(salesPerson).collect {
                when(it){
                    is ResultState.Loading -> {
                        _signUpstate.value = SignUpAuthState(isLoading = true)
                    }
                    is ResultState.Success<Boolean> -> {
                        _signUpstate.value = SignUpAuthState(Success = it.data, isLoading = false )
                    }
                    is ResultState.Error ->{
                        _signUpstate.value  = SignUpAuthState(error =it.message.toString(), isLoading = false)
                    }
                }
            }
        }

    }

    fun logInSalePerson(email: String, password: String){
        viewModelScope.launch {
            logInSalePersonUseCase.logInSalePerson(email, password).collect {
                when(it){
                    is ResultState.Loading -> {
                        _LogInAuthState.value = LogInAuthState(isLoading = true)
                    }
                    is ResultState.Success<Boolean> ->{
                        _LogInAuthState.value = LogInAuthState(Success = it.data, isLoading = false)

                    }
                    is ResultState.Error -> {
                        _LogInAuthState.value = LogInAuthState(error = it.message.toString(), isLoading = false)
                    }


                }
            }
        }

    }



}

data class LogInAuthState(
    val isLoading: Boolean? = false,
    val Success: Boolean? = null,
    val error: String?= null
)

data class SignUpAuthState(
    val isLoading: Boolean? = false,
    val Success: Boolean? = null,
    val error: String?= null
)