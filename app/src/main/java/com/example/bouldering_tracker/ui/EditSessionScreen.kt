package com.example.bouldering_tracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
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
fun EditSessionScreen(viewModel: SessionViewModel, sessionIndex: Int, navController: NavHostController, modifier:Modifier = Modifier){
    val sessionsData by viewModel.sessionsData.observeAsState(initial = emptyList())

    if (sessionsData.isEmpty() || sessionIndex !in sessionsData.indices) return

    val currentSession = sessionsData[sessionIndex]


    var location by rememberSaveable(currentSession.location) {
        mutableStateOf(currentSession.location)
    }

    var selectedDate by remember { mutableStateOf(currentSession.date) }

    var showDatePicker by remember {mutableStateOf(false)}

    val formatter = remember { SimpleDateFormat("dd/MM/yy", Locale.getDefault()) }
    var selectedDateText by rememberSaveable {
        mutableStateOf(formatter.format(currentSession.date))
    }

    var showDurationPicker by remember { mutableStateOf(false) }
    var durationText by rememberSaveable { mutableStateOf(currentSession.duration) }
    val initialHour = durationText.substringBefore("h").toIntOrNull() ?: 0
    val initialMinute = durationText.substringAfter("h ")
        .substringBefore("m")
        .toIntOrNull() ?: 0

    val durationState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    if (showDatePicker) {
        DatePickerModalInput(
            onDateSelected = { millis ->
                if (millis != null) {
                    val date = Date(millis)
                    selectedDate = date
                    val formatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                    selectedDateText = formatter.format(date)
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
            text = "Edit Session",
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

        Row () {
            Button(
                onClick = {
                    // Update the session with the values entered
                    viewModel.editSession(currentSession.copy(
                        location = location,
                        date = selectedDate,
                        duration = durationText
                    ))
3
                    navController.navigate("${AppScreens.AddClimb.name}/${sessionIndex}")
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
        if (currentSession.climbs.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(
                    items = currentSession.climbs,
                    key = { index, climb -> "${climb.grade}-${climb.colour}-$index" }
                ) { climbIndex, climb ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) { // Swipe Left
                                viewModel.deleteClimb(currentSession, climbIndex)
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
                            Row {
                                Column {
                                    Text(
                                        text = "V${climb.grade} - ${climb.colour} Holds",
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

                                // This Spacer pushes the edit icon to the far right
                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(
                                    onClick = { navController.navigate("EditClimb/$sessionIndex/$climbIndex") },
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(end = 12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
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
                viewModel.editSession(currentSession.copy(
                    location = location,
                    date = selectedDate,
                    duration = durationText
                ))
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