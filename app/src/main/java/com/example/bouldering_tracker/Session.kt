package com.example.bouldering_tracker

import java.util.Date

data class Session(
    val location: String,
    val date: String,
    val duration: String,
    val climbs: List<Climb>
)
