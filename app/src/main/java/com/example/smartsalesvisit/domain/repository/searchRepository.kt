package com.example.smartsalesvisit.domain.repository

import com.example.smartsalesvisit.common.ResultState
import kotlinx.coroutines.flow.Flow

interface searchRepository {

    fun transcribeAudio(audioPath: String) : Flow<ResultState<String>>
}