package com.example.smartsalesvisit.data.localStorage.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [VisitEntity::class],
    version = 2
)

abstract class AppDatabase: RoomDatabase(){

    abstract fun visitDao(): VisitDao
}