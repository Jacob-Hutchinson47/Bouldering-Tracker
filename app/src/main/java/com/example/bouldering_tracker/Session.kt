package com.example.bouldering_tracker

import java.util.Date

data class Session(
    var location: String,
    var date: Date,
    var duration: String,
    var climbs: MutableList<Climb>
)
