package com.example.bouldering_tracker.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingViewModel: SettingViewModel = viewModel(), navController: NavHostController, modifier:Modifier = Modifier){
    val context = LocalContext.current
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val location by settingViewModel.location.observeAsState("")
    val remindersEnabled by settingViewModel.remindersEnabled.observeAsState(true)
    val savedHour by settingViewModel.reminderHour.observeAsState(16)
    val savedMinute by settingViewModel.reminderMinute.observeAsState(0)
    var text by rememberSaveable {mutableStateOf (location)}

    // Update the default location textbox when the location is finished reading from the disk
    LaunchedEffect(location) {
        // Only update if the current text is empty to avoid overwriting what the user is currently typing
        if (text.isEmpty() && location.isNotEmpty()) {
            text = location
        }
    }

    var showTimePicker by remember {mutableStateOf(false)} // Track visibility of reminder time picker

    val timePickerState = rememberTimePickerState(
        initialHour = savedHour,
        initialMinute = savedMinute,
        is24Hour = true,
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // If denied turn the toggle back to off in the DataStore
            settingViewModel.saveReminderSettings(false, savedHour, savedMinute)
        }
    }

    Column (modifier=
        modifier.padding(16.dp)//add padding all around
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack, contentDescription = null,
            modifier = Modifier
                .clickable(
                    onClick = {
                        navController.popBackStack()
                    })
        )
        Text(
            text = "Settings",
            modifier = modifier.padding(bottom = 12.dp)
                .fillMaxWidth(1f),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Default Location:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        TextField(
            value = text,
            maxLines = 1,
            modifier = modifier
                .padding(bottom = 36.dp)
                .fillMaxWidth(1f),
            onValueChange = {
                text = it//update the value of the TextField
                settingViewModel.saveLocation(text) },//save the new location into the preference datastore
        )
        Text(
            text = "Session Reminders:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        Row() {
            Switch(
                checked = remindersEnabled,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        // 1. Check Exact Alarm Permission (Android 12+)[cite: 6, 8]
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                            context.startActivity(intent)
                            // We return early because we want the user to grant permission first
                            return@Switch
                        }

                        // 2. Check Notification Permission (Android 13+)[cite: 6, 8]
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }

                        settingViewModel.saveReminderSettings(true, savedHour, savedMinute)
                    } else {
                        settingViewModel.saveReminderSettings(false, savedHour, savedMinute)
                    }
                }
            )
            Text(
                text = "Enable Daily Session Reminders",
                modifier = Modifier.padding(8.dp)
            )
        }
        Text(
            text = "Reminder Time: (24h format)",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        Card(
            onClick = {showTimePicker = true},
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(
                text = "${savedHour.toString().padStart(2, '0')}:${savedMinute.toString().padStart(2, '0')}",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp
            )
        }
        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                onConfirm = {
                    showTimePicker = false
                    settingViewModel.saveReminderSettings(
                        remindersEnabled,
                        timePickerState.hour,
                        timePickerState.minute
                    )
                }
            ) {
                TimeInput(state = timePickerState)
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text("Cancel") }
        },
        text = {content()}
    )
}