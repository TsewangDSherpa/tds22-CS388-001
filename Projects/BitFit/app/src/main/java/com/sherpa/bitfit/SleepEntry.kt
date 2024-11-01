package com.sherpa.bitfit

data class SleepEntry(
    val id: Long,
    val date: String,
    val hoursSlept: Float,
    val feeling: String,
    val notes: String,
    var imageUrl: String

)
