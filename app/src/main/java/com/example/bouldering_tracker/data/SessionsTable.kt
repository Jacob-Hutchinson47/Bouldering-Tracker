package com.example.bouldering_tracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val location: String,
    val date: Date,
    val duration: String,
    val climbs: MutableList<Climb>
)