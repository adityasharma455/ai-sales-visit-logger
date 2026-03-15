package com.example.smartsalesvisit.data.di

import androidx.room.Room
import com.example.smartsalesvisit.data.AI.repoImpl.AiRepositoryImpl
import com.example.smartsalesvisit.common.SyncManager
import com.example.smartsalesvisit.data.localStorage.local.AppDatabase
import com.example.smartsalesvisit.data.localStorage.local.VisitDao
import com.example.smartsalesvisit.data.localStorage.repositoryImplementation.VisitRepositoryImpl
import com.example.smartsalesvisit.data.serverRepository.serverRepoImpl
import com.example.smartsalesvisit.domain.repository.AiRepository
import com.example.smartsalesvisit.domain.repository.LocalStorageRepository
import com.example.smartsalesvisit.domain.repository.ServerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "visit_database"
        ) .fallbackToDestructiveMigration()
            .build()
    }


    single<VisitDao> { get<AppDatabase>().visitDao() }


    single< LocalStorageRepository> { VisitRepositoryImpl(dao = get()) }

    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }

    single<ServerRepository> {
        serverRepoImpl(auth = get(), firebase = get())
    }

    single<AiRepository> {
        AiRepositoryImpl()
    }

    single<SyncManager> { SyncManager(
        visitRepository = get(),
        serverRepository = get(),
        aiVisitUseCase = get()
    ) }

}