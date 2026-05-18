package com.example.bouldering_tracker.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.bouldering_tracker.data.Converters
import com.example.bouldering_tracker.data.Session
import com.example.bouldering_tracker.provider.SessionsContract
import com.example.bouldering_tracker.data.SessionsDao
import com.example.bouldering_tracker.data.SessionsDatabase
import java.util.Date

class SessionsProvider : ContentProvider() {
    private lateinit var sessionsDao: SessionsDao

    override fun onCreate(): Boolean {
        sessionsDao = SessionsDatabase.getDatabase(context!!).sessionsDao()
        return true
    }

    companion object {
        private const val SESSIONS = 100
        private const val SESSION_ID = 101

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(SessionsContract.AUTHORITY, SessionsContract.Sessions.PATH_SESSIONS, SESSIONS)
            addURI(SessionsContract.AUTHORITY, "${SessionsContract.Sessions.PATH_SESSIONS}/#", SESSION_ID)
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            SESSIONS -> sessionsDao.getAllSessionsCursor()
            SESSION_ID -> {
                val id = ContentUris.parseId(uri).toInt()
                sessionsDao.getSessionItemCursor(id)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) != SESSIONS) {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
        val ctx = context ?: return null
        if (values == null) return null

        val location = values.getAsString(SessionsContract.Sessions.COLUMN_LOCATION) ?: "Unknown Gym"
        val duration = values.getAsString(SessionsContract.Sessions.COLUMN_DURATION) ?: "0h 0m"

        val dateLong = values.getAsLong(SessionsContract.Sessions.COLUMN_DATE)
        val sessionDate = if (dateLong != null) Date(dateLong) else Date()

        val climbsJson = values.getAsString(SessionsContract.Sessions.COLUMN_CLIMBS) ?: "[]"
        val parsedClimbsList = Converters().toClimbList(climbsJson)

        val newSession = Session(
            id = 0,
            location = location,
            date = sessionDate,
            duration = duration,
            climbs = parsedClimbsList
        )

        val rowId = sessionsDao.insertSessionSynchronous(newSession)
        if (rowId > -1) {
            ctx.contentResolver.notifyChange(uri, null)
            return ContentUris.withAppendedId(uri, rowId)
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (uriMatcher.match(uri)) {
            SESSION_ID -> {
                val sessionId = ContentUris.parseId(uri).toInt()
                val deletedRows = sessionsDao.deleteSessionById(sessionId)
                if (deletedRows > 0) {
                    context?.contentResolver?.notifyChange(uri, null)
                }
                deletedRows
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Update operations are currently not supported via provider mechanisms")
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            SESSIONS -> SessionsContract.Sessions.CONTENT_TYPE
            SESSION_ID -> SessionsContract.Sessions.CONTENT_ITEM_TYPE
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}