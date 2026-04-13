package com.example.bouldering_tracker

import java.util.Date

data class Session(
    var location: String,
    var date: String,
    var duration: String,
    var climbs: MutableList<Climb>
)
