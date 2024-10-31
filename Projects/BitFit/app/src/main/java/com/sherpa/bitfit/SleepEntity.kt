package com.sherpa.bitfit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_table")
data class SleepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val hoursSlept: Float,
    val feeling: String,
    val notes: String,
)