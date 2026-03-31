package com.example.smartsalesvisit.data.localStorage.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [VisitEntity::class],
    version = 3
)

abstract class AppDatabase: RoomDatabase(){

    abstract fun visitDao(): VisitDao
}