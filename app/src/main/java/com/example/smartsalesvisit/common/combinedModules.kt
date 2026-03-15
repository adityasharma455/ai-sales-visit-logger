package com.example.smartsalesvisit.common

import com.example.smartsalesvisit.data.di.dataModule
import com.example.smartsalesvisit.domain.di.domainModule
import com.example.smartsalesvisit.presentation.di.presentationModule

val combinedModules = listOf(
    dataModule,
    presentationModule,
    domainModule
)