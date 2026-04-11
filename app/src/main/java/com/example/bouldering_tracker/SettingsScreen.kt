package com.example.bouldering_tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, modifier:Modifier = Modifier){
    var defaultLocation by rememberSaveable {mutableStateOf("The Climbing Station")}

    var sessionRemindersEnabled by remember {mutableStateOf(false)}

    var showTimePicker by remember {mutableStateOf(false)} // Track visibility of reminder time picker

    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
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
            value = defaultLocation,
            maxLines = 1,
            onValueChange = { defaultLocation = it },
            modifier = modifier
                .padding(bottom = 36.dp)
                .fillMaxWidth(1f)
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