package com.example.bouldering_tracker.ui

import androidx.lifecycle.ViewModel
import com.example.bouldering_tracker.Climb
import com.example.bouldering_tracker.ClimbStatus
import com.example.bouldering_tracker.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow("")
    val uiState = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    private val _sessionsData = MutableStateFlow<List<Session>>(mutableListOf(
        Session("The Climbing Station", dateFormatter.parse("13/03/26"), "1h 45m", mutableListOf(
            Climb(3, "Red", 2, ClimbStatus.Sent, "Just go up"),
            Climb(4, "Green", 3, ClimbStatus.Flashed, "Don't fall off")
        )),
        Session("The Climbing Station", dateFormatter.parse("08/03/26"), "2h", mutableListOf()),

        // New Data Starts Here
        Session("Big Rock Hub", dateFormatter.parse("20/03/26"), "2h 15m", mutableListOf(
            Climb(2, "Blue", 1, ClimbStatus.Flashed, "Warm up"),
            Climb(4, "Black", 5, ClimbStatus.Sent, "Hard crimpy move at the top"),
            Climb(5, "Yellow", 8, ClimbStatus.Project, "Need more finger strength for the start")
        )),
        Session("The Depot", dateFormatter.parse("27/03/26"), "1h 30m", mutableListOf(
            Climb(3, "Purple", 2, ClimbStatus.Sent, "Nice slab"),
            Climb(3, "Purple", 1, ClimbStatus.Flashed, "Easy dynamic move"),
            Climb(4, "White", 4, ClimbStatus.Sent, "Technical footwork required")
        )),
        Session("Big Rock Hub", dateFormatter.parse("02/04/26"), "2h", mutableListOf(
            Climb(4, "Green", 2, ClimbStatus.Sent, "Repeat from last time"),
            Climb(5, "Yellow", 10, ClimbStatus.Sent, "FINALLY SENT IT!"),
            Climb(6, "Orange", 3, ClimbStatus.Project, "New highest grade attempt")
        )),
        Session("The Climbing Station", dateFormatter.parse("05/04/26"), "1h 10m", mutableListOf(
            Climb(3, "Red", 1, ClimbStatus.Flashed, "Quick session"),
            Climb(4, "Green", 2, ClimbStatus.Sent, "Feeling strong")
        )),
        Session("Flashpoint", dateFormatter.parse("10/04/26"), "2h 30m", mutableListOf(
            Climb(2, "Blue", 1, ClimbStatus.Flashed, "Good reset"),
            Climb(3, "Red", 1, ClimbStatus.Flashed, "Soft for the grade"),
            Climb(4, "Black", 6, ClimbStatus.Sent, "Burly overhang"),
            Climb(5, "Yellow", 4, ClimbStatus.Project, "Pumped out at the end")
        ))
    ))
    val sessionsData = _sessionsData.asStateFlow()

    private val _draftSession = MutableStateFlow(Session("", Date(), "0h0m", mutableListOf()))
    val draftSession = _draftSession.asStateFlow()

    fun updateDraftDetails(location: String, date: Date, duration: String) {
        _draftSession.value = _draftSession.value.copy(
            location = location,
            date = date,
            duration = duration
        )
    }

    fun addClimbToDraft(climb: Climb) {
        val currentDraft = _draftSession.value
        currentDraft.climbs.add(climb)
        _draftSession.value = currentDraft
    }

    fun saveDraftAsNewSession() {
        val completedSession = _draftSession.value
        _sessionsData.value = _sessionsData.value + completedSession

        // Reset the draft for the next use
        _draftSession.value = Session("", Date(), "", mutableListOf())
    }

    fun addClimbToSession(sessionIndex: Int, climb: Climb) {
        val currentSessions = _sessionsData.value.toMutableList()

        if (sessionIndex in currentSessions.indices) {
            currentSessions[sessionIndex].climbs.add(climb)
            _sessionsData.value = currentSessions.toList()
        }
    }
}