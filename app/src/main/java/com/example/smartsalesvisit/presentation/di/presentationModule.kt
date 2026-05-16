package com.example.smartsalesvisit.presentation.di

import com.example.smartsalesvisit.domain.useCase.ChatBotUseCase
import com.example.smartsalesvisit.presentation.managerScreens.ManagerViewModel
import com.example.smartsalesvisit.presentation.managerScreens.summaryScreen.ManagerSummaryViewModel
import com.example.smartsalesvisit.presentation.screens.AddVisitScreen.RegisterVisitViewModel
import com.example.smartsalesvisit.presentation.screens.AuthScreen.authViewModel
import com.example.smartsalesvisit.presentation.screens.ChatBotScreen.ChatBotViewModel
import com.example.smartsalesvisit.presentation.screens.SeeAllVisits.VisitViewModel
import com.example.smartsalesvisit.presentation.screens.UpdateVisits.UpdateVisitsViewModel
import com.example.smartsalesvisit.presentation.screens.VistsByDate.SalesAnalyticsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module{
    viewModel<RegisterVisitViewModel> { RegisterVisitViewModel(
        addVisitUseCase = get(),
        updateVisitUseCase = get(),
        aiVisitUseCase = get(),
        context = androidContext(),
        uploadVisitOnServerUseCase = get(),
        tanscribeAudioUserUseCase = get()
    ) }
    viewModel<VisitViewModel> { VisitViewModel(
        getVisitUseCase =get()
    ) }

    viewModel { UpdateVisitsViewModel(
        updateVisitUseCase = get(),
        aiVisitUseCase = get(),
        uploadVisitOnServerUseCase = get(),
        context = androidContext()
    ) }

    viewModel { authViewModel(
        registerSalePersonUseCase = get(),
        logInSalePersonUseCase = get()
    ) }

    viewModel { ManagerViewModel(
        getAllVisitsByTerritoryUseCase = get(),
        getSalePersonByIdUseCase = get()
    ) }

    viewModel {
        SalesAnalyticsViewModel(
            getVisitsByDateForCurrentUserUseCase = get()
        )
    }

    viewModel<ChatBotViewModel> {
        ChatBotViewModel(
            chatBotUseCase = get()
        )
    }

    viewModel<ManagerSummaryViewModel> { ManagerSummaryViewModel(
        getSummaryByDateUserUseCase = get()
    ) }


}