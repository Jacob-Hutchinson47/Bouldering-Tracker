package com.example.bouldering_tracker.data

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SessionsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(session: Session)

    //fetch all session information
    @Query("SELECT * FROM sessions")
    fun getSessions(): LiveData<List<Session>>

    //delete all session information
    @Query("DELETE FROM sessions")
    suspend fun deleteAll()

    @Update
    suspend fun update(session: Session)

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSessionById(id: Int): Session?

    @Delete
    suspend fun delete(session: Session)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSessionSynchronous(session: Session): Long

    @Query("SELECT * FROM Sessions")
    fun getAllSessionsCursor(): Cursor

    @Query("SELECT * FROM Sessions WHERE id = :id")
    fun getSessionItemCursor(id: Int): Cursor

    @Query("DELETE FROM Sessions WHERE id = :id")
    fun deleteSessionById(id: Int): Int
}
