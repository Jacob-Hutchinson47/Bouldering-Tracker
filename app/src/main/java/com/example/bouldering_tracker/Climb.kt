package com.example.bouldering_tracker

data class Climb(
    val grade: Int,
    val colour: String,
    val attempts: Int,
    val status: ClimbStatus,
    val note: String,
)

enum class ClimbStatus{
    Flashed,
    Sent,
    Project,
}
