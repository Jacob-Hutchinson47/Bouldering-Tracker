package com.example.bouldering_tracker.data

import androidx.lifecycle.LiveData

class SessionsRepository(private val sessionsDao: SessionsDao) {
    val allSessions: LiveData<List<Session>> = sessionsDao.getWeather()
    suspend fun insertSession(session:Session){
        sessionsDao.insert(session)
    }
    suspend fun deleteSession(session: Session) {
        sessionsDao.delete(session)
    }
    suspend fun updateSession(session: Session) {
        sessionsDao.update(session)
    }
}