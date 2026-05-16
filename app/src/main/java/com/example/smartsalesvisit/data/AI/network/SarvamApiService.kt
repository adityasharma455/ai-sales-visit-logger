package com.example.smartsalesvisit.data.AI.network


import com.example.smartsalesvisit.domain.models.SarvamResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SarvamApiService {

    @Multipart
    @POST("speech-to-text")
    suspend fun transcribeAudio(
        @Part file: MultipartBody.Part
    ): retrofit2.Response<SarvamResponse>
}