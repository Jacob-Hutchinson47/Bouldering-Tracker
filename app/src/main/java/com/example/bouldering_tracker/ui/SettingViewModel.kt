package com.example.bouldering_tracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bouldering_tracker.data.LocationPreferenceManager
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    private val locationPreferenceManager = LocationPreferenceManager(application)

    val location: LiveData<String> = locationPreferenceManager.locationFlow
        .map { it ?: "The Climbing Station" } // Provide a default value if null
        .asLiveData()

    fun saveLocation(newLocation: String) {
        viewModelScope.launch {
            locationPreferenceManager.saveLocation(newLocation)
        }
    }
}