package com.example.smartsalesvisit.presentation.screens.ChatBotScreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartsalesvisit.common.ResultState
import com.example.smartsalesvisit.domain.useCase.ChatBotUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatBotViewModel(
    private val chatBotUseCase: ChatBotUseCase
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    fun sendMessage(message: String) {

        _chatState.value = _chatState.value.copy(
            messages = _chatState.value.messages + (message to true)
        )

        viewModelScope.launch {

            chatBotUseCase.chatBotUseCase(message).collect { result ->

                when (result) {

                    is ResultState.Loading -> {
                        _chatState.value = _chatState.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _chatState.value = _chatState.value.copy(
                            isLoading = false,
                            messages = _chatState.value.messages + (result.data to false)
                        )
                    }

                    is ResultState.Error -> {
                        _chatState.value = _chatState.value.copy(
                            isLoading = false,
                            messages = _chatState.value.messages + ("Error: ${result.message}" to false)
                        )
                    }
                }
            }
        }
    }
}

data class ChatState(
    val messages: List<Pair<String, Boolean>> = emptyList(),
    val isLoading: Boolean = false
)