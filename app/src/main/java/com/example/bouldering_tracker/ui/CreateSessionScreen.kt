package com.example.bouldering_tracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bouldering_tracker.AppScreens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSessionScreen(viewModel: SessionViewModel, settingViewModel: SettingViewModel = viewModel(), navController: NavHostController, modifier:Modifier = Modifier){
    val draftSession by viewModel.draftSession.collectAsState()
    val defaultLocation by settingViewModel.location.observeAsState("")

    var location by rememberSaveable {mutableStateOf(draftSession.location)}

    // Update the location when the location is finished reading from the disk
    LaunchedEffect(defaultLocation) {
        if (location.isEmpty() && defaultLocation.isNotEmpty()) {
            location = defaultLocation
        }
    }

    var selectedDate by remember { mutableStateOf(draftSession.date) }

    var showDatePicker by remember {mutableStateOf(false)}
    val today = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date())
    var selectedDateText by rememberSaveable {mutableStateOf(today)}

    var showDurationPicker by remember { mutableStateOf(false) }
    var durationText by rememberSaveable { mutableStateOf(draftSession.duration) }
    val durationState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = true // Use 24h to avoid AM/PM confusion for duration
    )

    if (showDatePicker) {
        DatePickerModalInput(
            onDateSelected = { millis ->
                if (millis != null) {
                    selectedDate = Date(millis)
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Column (modifier=
        modifier
            .padding(16.dp)//add padding all around
            .fillMaxSize()
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
            text = "Create Session",
            modifier = modifier.padding(bottom = 12.dp)
                .fillMaxWidth(1f),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
        )
        Row () {
            Text(
                text = "Location:",
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = location,
                maxLines = 1,
                onValueChange = { location = it },
                modifier = modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(1f)
            )
        }
        Row () {
            Text(
                text = "Session Date:",
                modifier = Modifier.padding(8.dp)
            )
            Card(
                onClick = { showDatePicker = true }, // Opens the picker
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = selectedDateText,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp
                )
            }
            if (showDatePicker) {
                DatePickerModalInput(
                    onDateSelected = { millis ->
                        if (millis != null) {
                            // Convert milliseconds to a readable date string
                            val date = Date(millis)
                            val formatter = SimpleDateFormat(
                                "dd/MM/yy",
                                Locale.getDefault()
                            )
                            selectedDateText = formatter.format(date)
                        }
                        showDatePicker = false // Dismisses the picker after selection
                    },
                    onDismiss = { showDatePicker = false } // Dismisses if they click away/Cancel
                )
            }
        }
        Row () {
            Text(
                text = "Duration:",
                modifier = Modifier.padding(8.dp)
            )
            Card(
                onClick = {showDurationPicker = true},
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(text = durationText, modifier = Modifier.padding(16.dp))
            }
        }

        if (showDatePicker) {
            DatePickerModalInput(
                onDateSelected = { millis ->
                    millis?.let {
                        val formatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                        selectedDateText = formatter.format(Date(it))
                    }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }

        if (showDurationPicker) {
            DurationPickerDialog(
                onDismissRequest = { showDurationPicker = false },
                onConfirm = {
                    durationText = "${durationState.hour}h ${durationState.minute}m"
                    showDurationPicker = false
                }
            ) {
                TimeInput(state = durationState)
            }
        }

        Row () {
            Button(
                onClick = {
                    // Update the draft session with the values entered
                    viewModel.updateDraftDetails(location, selectedDate, durationText)

                    navController.navigate("${AppScreens.AddClimb.name}/-1")
                },
                modifier
                    .weight(1f, true)
                    .height(64.dp)
                    .padding(8.dp)
            ) {
                Text(text = "Add Climb",
                    textAlign = TextAlign.Center)
            }
        }

        Text(
            text = "Climbs:",
            fontWeight = FontWeight.Bold,
            modifier =  Modifier.padding(8.dp)
        )
        if (draftSession.climbs.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(
                    items = draftSession.climbs,
                    key = { index, climb -> "${climb.grade}-${climb.colour}-$index" }
                ) { climbIndex, climb ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) { // Swipe Left
                                viewModel.deleteClimbFromDraft(climbIndex)
                                true
                            } else {
                                false
                            }
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false, // Disable swiping right
                        backgroundContent = {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error // Red icon
                                )
                            }
                        }
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "V" + climb.grade + " - " + climb.colour + " Holds",
                                modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                            )
                            Text(
                                text = "Attempts: " + climb.attempts,
                                modifier = Modifier.padding(start = 12.dp),
                            )
                            Text(
                                text = "Status: " + climb.status.name,
                                modifier = Modifier.padding(start = 12.dp, bottom = 12.dp),
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = "No Climbs",
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f),
            )
        }

        Button(
            onClick = {
                viewModel.updateDraftDetails(location, selectedDate, durationText)
                viewModel.saveDraftAsNewSession()
                navController.popBackStack() // Go back to home screen
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(84.dp)
        ) {
            Text("Save Session")
        }
    }
}

@Composable
fun DurationPickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        title = { Text("Set Duration") },
        text = { content() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    // Initialize the state with the current time in milliseconds
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        initialDisplayMode = DisplayMode.Input
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
