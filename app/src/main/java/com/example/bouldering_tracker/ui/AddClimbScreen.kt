package com.example.bouldering_tracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bouldering_tracker.Climb
import com.example.bouldering_tracker.ClimbStatus

@Composable
fun AddClimbScreen(viewModel: SessionViewModel, sessionIndex: Int, navController: NavHostController, modifier:Modifier = Modifier) {
    val sessionsData by viewModel.sessionsData.collectAsState()

    var grade by rememberSaveable {mutableStateOf("")}

    var colour by rememberSaveable {mutableStateOf("")}

    var attempts by rememberSaveable {mutableStateOf("")}

    val resultOptions = listOf(ClimbStatus.Flashed.name, ClimbStatus.Sent.name, ClimbStatus.Project.name)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(resultOptions[0]) }

    var notes by rememberSaveable {mutableStateOf("")}

    Column(
        modifier =
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
            text = "Add Climb",
            modifier = modifier.padding(bottom = 12.dp)
                .fillMaxWidth(1f),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
        )
        Row () { // Grade
            Text(
                text = "Grade:",
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = grade,
                maxLines = 1,
                onValueChange = {grade = it},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(1f)
            )
        }
        Row () { // Colour
            Text(
                text = "Colour:",
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = colour,
                maxLines = 1,
                onValueChange = { colour = it },
                modifier = modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(1f)
            )
        }
        Row () { // Attempts
            Text(
                text = "Attempts:",
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = attempts,
                maxLines = 1,
                onValueChange = {attempts = it},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(1f)
            )
        }

        Text(
            text = "Result:",
            fontWeight = FontWeight.Bold,
            modifier =  Modifier.padding(8.dp)
        )
        // Result selection radio options
        Column(modifier.selectableGroup()) {
            resultOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        Text(
            text = "Notes:",
            fontWeight = FontWeight.Bold,
            modifier =  Modifier.padding(8.dp)
        )
        TextField(
            value = notes,
            maxLines = 1,
            onValueChange = { notes = it },
            modifier = modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(1f)
                .weight(1f)
        )

        Button(
            onClick = {
                val newClimb = Climb(
                    grade = grade.toIntOrNull() ?: 0,
                    colour = colour,
                    attempts = attempts.toIntOrNull() ?: 1,
                    status = ClimbStatus.valueOf(selectedOption),
                    note = notes // This fixes your "No value passed" error
                )

                if (sessionIndex == -1) {
                    viewModel.addClimbToDraft(newClimb) // New session
                } else {
                    viewModel.addClimbToSession(sessionIndex, newClimb) // Existing session
                }

                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(84.dp)
        ) {
            Text("Save Climb")
        }
    }
}