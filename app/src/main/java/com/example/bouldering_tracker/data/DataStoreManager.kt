package com.example.bouldering_tracker.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "locationPreference")

class LocationPreferenceManager(private val context: Context) {
    companion object {
        private val LOCATION_KEY = stringPreferencesKey("location")
    }
    val locationFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            // Retrieve the location value, returning null if not set
            preferences[LOCATION_KEY]
        }
    suspend fun saveLocation(location: String) {
        context.dataStore.edit { preferences ->
            // Save the location in DataStore
            preferences[LOCATION_KEY] = location
        }
    }

}