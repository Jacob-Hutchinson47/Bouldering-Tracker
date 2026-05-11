package com.example.bouldering_tracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bouldering_tracker.data.Climb
import com.example.bouldering_tracker.data.ClimbStatus
import com.example.bouldering_tracker.data.DraftSession
import com.example.bouldering_tracker.data.Session
import com.example.bouldering_tracker.data.SessionsDatabase
import com.example.bouldering_tracker.data.SessionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionsRepository: SessionsRepository

    val sessionsData: LiveData<List<Session>>

    init {
        val sessionsDao = SessionsDatabase.getDatabase(application).sessionsDao()
        sessionsRepository = SessionsRepository(sessionsDao)
        sessionsData = sessionsRepository.allSessions
    }

    private val _draftSession = MutableStateFlow(DraftSession("", Date(), "0h 0m", mutableListOf()))

    val draftSession = _draftSession.asStateFlow()

    fun updateDraftDetails(location: String, date: Date, duration: String) {
        _draftSession.value = _draftSession.value.copy(
            location = location,
            date = date,
            duration = duration
        )
    }

    fun addClimbToDraft(climb: Climb) {
        val updatedClimbs = _draftSession.value.climbs.toMutableList()
        updatedClimbs.add(climb)
        _draftSession.value = _draftSession.value.copy(climbs = updatedClimbs)
    }

    fun saveDraftAsNewSession() {
        viewModelScope.launch {
            val draft = _draftSession.value
            val newSession = Session(
                id = 0,
                location = draft.location,
                date = draft.date,
                duration = draft.duration,
                climbs = draft.climbs
            )
            sessionsRepository.insertSession(newSession)

            // Reset draft
            _draftSession.value = DraftSession("", Date(), "0h 0m", mutableListOf())
        }
    }

    fun addClimbToSession(sessionIndex: Int, climb: Climb) {
        viewModelScope.launch {
            val currentList = sessionsData.value
            if (currentList != null && sessionIndex in currentList.indices) {
                val sessionToUpdate = currentList[sessionIndex]
                sessionToUpdate.climbs.add(climb)
                sessionsRepository.updateSession(sessionToUpdate)
            }
        }
    }

    fun deleteSession(session: Session) {
        viewModelScope.launch {
            sessionsRepository.deleteSession(session)
        }
    }

    fun deleteClimb(session: Session, climbIndex: Int) {
        viewModelScope.launch {
            val updatedClimbs = session.climbs.toMutableList()

            if (climbIndex in updatedClimbs.indices) {
                updatedClimbs.removeAt(climbIndex)

                val updatedSession = session.copy(climbs = updatedClimbs)

                sessionsRepository.updateSession(updatedSession)
            }
        }
    }

    fun deleteClimbFromDraft(index: Int) {
        val updatedClimbs = _draftSession.value.climbs.toMutableList()
        if (index in updatedClimbs.indices) {
            updatedClimbs.removeAt(index)
            _draftSession.value = _draftSession.value.copy(climbs = updatedClimbs)
        }
    }

    fun editSession(session: Session) {
        viewModelScope.launch {
            sessionsRepository.updateSession(session)
        }
    }

    fun addClimbToSession(climb: Climb) {
        val updatedClimbs = _draftSession.value.climbs.toMutableList()
        updatedClimbs.add(climb)
        _draftSession.value = _draftSession.value.copy(climbs = updatedClimbs)
    }
}