package com.example.smartsalesvisit.data.localStorage.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: VisitEntity)

    @Query("SELECT * FROM visits ORDER BY visitDate DESC")
    fun getAllVisits(): Flow<List<VisitEntity>>

    @Update
    suspend fun updateVisit(visit: VisitEntity)

    @Query("SELECT * FROM visits WHERE id = :id")
    suspend fun getVisitById(id: String): VisitEntity

    @Query("""
SELECT * FROM visits 
WHERE syncStatus != 'SYNCED'
ORDER BY visitDate DESC
""")
    suspend fun getUnsyncedVisits(): List<VisitEntity>
}