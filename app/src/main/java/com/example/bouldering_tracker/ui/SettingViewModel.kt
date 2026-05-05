package com.example.bouldering_tracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bouldering_tracker.data.SettingsPreferenceManager
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    private val preferenceManager = SettingsPreferenceManager(application)

    val location: LiveData<String> = preferenceManager.locationFlow
        .map { it ?: "The Climbing Station" } // Provide a default value if null
        .asLiveData()

    val remindersEnabled: LiveData<Boolean> = preferenceManager.remindersEnabledFlow.asLiveData()
    val reminderHour: LiveData<Int> = preferenceManager.reminderHourFlow.asLiveData()
    val reminderMinute: LiveData<Int> = preferenceManager.reminderMinuteFlow.asLiveData()

    fun saveLocation(newLocation: String) {
        viewModelScope.launch {
            preferenceManager.saveLocation(newLocation)
        }
    }

    fun saveReminderSettings(enabled: Boolean, hour: Int, minute: Int) {
        viewModelScope.launch {
            preferenceManager.saveReminderSettings(enabled, hour, minute)

            val context = getApplication<Application>().applicationContext

            if (enabled) {
                ReminderScheduler.scheduleReminder(context, hour, minute)
            } else {
                ReminderScheduler.cancelReminder(context)
            }
        }
    }
}