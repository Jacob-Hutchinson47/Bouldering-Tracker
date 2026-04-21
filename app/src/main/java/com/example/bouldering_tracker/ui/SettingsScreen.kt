package com.example.bouldering_tracker.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingViewModel: SettingViewModel = viewModel(), navController: NavHostController, modifier:Modifier = Modifier){
    val location by settingViewModel.location.observeAsState("")
    var text by rememberSaveable {mutableStateOf (location)}

    // Update the default location textbox when the location is finished reading from the disk
    LaunchedEffect(location) {
        // Only update if the current text is empty to avoid overwriting what the user is currently typing
        if (text.isEmpty() && location.isNotEmpty()) {
            text = location
        }
    }

    var sessionRemindersEnabled by remember {mutableStateOf(true)}

    var showTimePicker by remember {mutableStateOf(false)} // Track visibility of reminder time picker

    val timePickerState = rememberTimePickerState(
        initialHour = 16,
        initialMinute = 0,
        is24Hour = true,
    )

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
                checked = sessionRemindersEnabled,
                onCheckedChange = {
                    sessionRemindersEnabled = it
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
                text = "${timePickerState.hour.toString().padStart(2, '0')}:${timePickerState.minute.toString().padStart(2, '0')}",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp
            )
        }
        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                onConfirm = { showTimePicker = false }
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