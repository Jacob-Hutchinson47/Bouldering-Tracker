package com.example.bouldering_tracker.data

import java.util.Date

data class DraftSession(
    val location: String,
    val date: Date,
    val duration: String,
    val climbs: MutableList<Climb>
)
