package com.example.bouldering_tracker.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settingsPreference")

class SettingsPreferenceManager(private val context: Context) {
    companion object {
        private val LOCATION_KEY = stringPreferencesKey("location")

        private val REMINDERS_ENABLED_KEY = booleanPreferencesKey("reminders_enabled")
        private val REMINDER_HOUR_KEY = intPreferencesKey("reminder_hour")
        private val REMINDER_MINUTE_KEY = intPreferencesKey("reminder_minute")
    }

    val locationFlow: Flow<String?> = context.dataStore.data
        .map {preferences ->
            preferences[LOCATION_KEY]
        }

    val remindersEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map {it[REMINDERS_ENABLED_KEY] ?: false} // Default to false

    val reminderHourFlow: Flow<Int> = context.dataStore.data
        .map {it[REMINDER_HOUR_KEY] ?: 16} // Default to 16:00

    val reminderMinuteFlow: Flow<Int> = context.dataStore.data
        .map {it[REMINDER_MINUTE_KEY] ?: 0}

    suspend fun saveLocation(location: String) {
        context.dataStore.edit { preferences ->
            preferences[LOCATION_KEY] = location
        }
    }

    suspend fun saveReminderSettings(enabled: Boolean, hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[REMINDERS_ENABLED_KEY] = enabled
            preferences[REMINDER_HOUR_KEY] = hour
            preferences[REMINDER_MINUTE_KEY] = minute
        }
    }
}