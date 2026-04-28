package com.example.bouldering_tracker.data

data class Climb(
    var grade: Int,
    var colour: String,
    var attempts: Int,
    var status: ClimbStatus,
    var note: String,
)

enum class ClimbStatus{
    Flashed,
    Sent,
    Project,
}
