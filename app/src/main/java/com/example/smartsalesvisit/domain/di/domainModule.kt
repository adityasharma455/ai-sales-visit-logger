package com.example.smartsalesvisit.domain.di

import com.example.smartsalesvisit.domain.useCase.AiVisitUseCase
import com.example.smartsalesvisit.domain.useCase.LogInSalePersonUseCase
import com.example.smartsalesvisit.domain.useCase.SyncVisitsUseCase
import com.example.smartsalesvisit.domain.useCase.UploadVisitOnServerUseCase
import com.example.smartsalesvisit.domain.useCase.addVisitUseCase
import com.example.smartsalesvisit.domain.useCase.getVisitByIdUseCase
import com.example.smartsalesvisit.domain.useCase.getVisitsUseCase
import com.example.smartsalesvisit.domain.useCase.registerSalePersonUseCase
import com.example.smartsalesvisit.domain.useCase.updateVisitUseCase
import org.koin.dsl.module

val domainModule = module{
    factory { addVisitUseCase(get()) }
    factory { getVisitsUseCase(get()) }
    factory { getVisitByIdUseCase(get()) }
    factory { updateVisitUseCase(get()) }
    factory { LogInSalePersonUseCase(get()) }
    factory { registerSalePersonUseCase(get()) }
    factory { AiVisitUseCase(get()) }
    factory { updateVisitUseCase(get()) }
    factory { UploadVisitOnServerUseCase(get()) }
    factory { SyncVisitsUseCase(get()) }

}