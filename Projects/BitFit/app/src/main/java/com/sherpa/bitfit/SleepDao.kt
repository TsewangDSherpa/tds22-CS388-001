package com.sherpa.bitfit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Query("SELECT * FROM sleep_table")
    fun getAll(): Flow<List<SleepEntity>>

    @Insert
    fun insert(articles: SleepEntity)

    @Query("DELETE FROM sleep_table WHERE id = :id")
    suspend fun delete(id: Long)
}