package com.example.bouldering_tracker

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bouldering_tracker.provider.SessionsContract
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class SessionsProviderTest {

    private lateinit var context: Context
    private lateinit var resolver: ContentResolver

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        resolver = context.contentResolver

        val values = ContentValues().apply {
            put(SessionsContract.Sessions.COLUMN_LOCATION, "The Climbing Station")
            put(SessionsContract.Sessions.COLUMN_DATE, System.currentTimeMillis())
            put(SessionsContract.Sessions.COLUMN_DURATION, "1h 45m")
            put(SessionsContract.Sessions.COLUMN_CLIMBS, "[]")
        }
        Log.d("SessionsProviderTest", "Setting up with initial row: $values")
        val uri = resolver.insert(SessionsContract.Sessions.CONTENT_URI, values)
        assertNotNull("Initial setup insert failed", uri)
    }

    @Test
    fun testInsertSession() {
        // Test insert a session
        val values = ContentValues().apply {
            put(SessionsContract.Sessions.COLUMN_LOCATION, "Nottingham Climbing Centre")
            put(SessionsContract.Sessions.COLUMN_DATE, System.currentTimeMillis())
            put(SessionsContract.Sessions.COLUMN_DURATION, "2h 15m")
            put(SessionsContract.Sessions.COLUMN_CLIMBS, "[]")
        }
        Log.d("SessionsProviderTest", "Testing insert with values: $values")

        val uri = resolver.insert(SessionsContract.Sessions.CONTENT_URI, values)
        assertNotNull("Insertion returned a null URI reference", uri)
        Log.d("SessionsProviderTest", "Returned insertion URI: $uri")

        val sessionId = ContentUris.parseId(uri!!)
        Log.d("SessionsProviderTest", "Parsed session primary ID: $sessionId")
        assertTrue("Session ID should be generated sequentially (> 0)", sessionId > 0)
    }

    @Test
    fun testDeleteSession() {
        // Insert a session
        val uri = SessionsContract.Sessions.CONTENT_URI
        val values = ContentValues().apply {
            put(SessionsContract.Sessions.COLUMN_LOCATION, "Temporary Flash Gym")
            put(SessionsContract.Sessions.COLUMN_DATE, System.currentTimeMillis())
            put(SessionsContract.Sessions.COLUMN_DURATION, "1h 0m")
            put(SessionsContract.Sessions.COLUMN_CLIMBS, "[]")
        }
        val insertUri = resolver.insert(uri, values)
        assertNotNull("Setup insert failed during delete evaluation preparation", insertUri)

        // Delete the session
        val deleteCount = resolver.delete(insertUri!!, null, null)
        assertEquals("Delete sequence failed to delete exactly 1 tracking session row", 1, deleteCount)

        // Attempt to query the deleted session
        val cursor = resolver.query(insertUri, null, null, null, null)
        assertNotNull("Returned matching item cursor evaluation block is null", cursor)
        assertFalse("Session element query remains accessible inside the provider state", cursor!!.moveToFirst())
        cursor.close()
    }

    @Test
    fun testQueryAllSessions() {
        val uri = SessionsContract.Sessions.CONTENT_URI
        val cursor = resolver.query(uri, null, null, null, null)

        assertNotNull("Query operation returned a null cursor structure", cursor)
        assertTrue("Cursor should return data elements including the baseline configuration index", cursor!!.moveToFirst())

        // Check column layout configuration references
        val locationIndex = cursor.getColumnIndexOrThrow(SessionsContract.Sessions.COLUMN_LOCATION)
        val durationIndex = cursor.getColumnIndexOrThrow(SessionsContract.Sessions.COLUMN_DURATION)

        assertNotNull("Location attribute cannot resolve parsing checks", cursor.getString(locationIndex))
        assertNotNull("Duration text description returns uninitialized state parameters", cursor.getString(durationIndex))

        cursor.close()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testQueryInvalidUri() {
        resolver.query(
            Uri.parse("content://${SessionsContract.AUTHORITY}/invalid_bouldering_table_endpoint"),
            null, null, null, null
        )
    }
}