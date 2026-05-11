package com.example.bouldering_tracker.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bouldering_tracker.data.Session
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(viewModel: SessionViewModel, navController: NavHostController, modifier: Modifier = Modifier) {
    val sessionsData by viewModel.sessionsData.observeAsState(initial = emptyList())

    Column (modifier=
        modifier.padding(16.dp)//add padding all around
    ){
        Text(
            text = "Bouldering Tracker",
            modifier = modifier.padding(bottom = 12.dp)
                .fillMaxWidth(1f),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
        )
        Row(){
            Button( // Create new session
                onClick = {
                    navController.navigate(route = "CreateSession")
                },
                modifier
                    .weight(1f, true)
                    .height(64.dp)
                    .padding(2.dp)
            ) {
                Text(text = "Create New Session",
                    textAlign = TextAlign.Center)
            }
            Button( // View Stats
                onClick = {
                    navController.navigate(route = "Stats")
                },
                modifier
                    .weight(1f, true)
                    .height(64.dp)
                    .padding(2.dp)
            ) {
                Text(text = "View Stats",
                    textAlign = TextAlign.Center)
            }
            Button( // Settings
                onClick = {
                    navController.navigate(route = "Settings")
                },
                modifier
                    .weight(1f, true)
                    .height(64.dp)
                    .padding(2.dp)
            ) {
                Text(text = "Settings",
                    textAlign = TextAlign.Center)
            }
        }
        Text(
            text = "Sessions:",
            fontWeight = FontWeight.Bold,
            modifier =  Modifier.padding(8.dp)
        )
        SessionsList(viewModel, sessionsData, navController, modifier)
    }
}

@Composable
fun SessionsList(viewModel: SessionViewModel, sessions:List<Session>, navController: NavHostController, modifier: Modifier){//create a lazy list of texts from the data
    if (sessions.count() > 0) {
        LazyColumn {
            itemsIndexed(items = sessions, key = { _, session -> session.id }) { index, session ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            viewModel.deleteSession(session)
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
                            .clickable {
                                navController.navigate("SessionInfo/$index")
                            }
                    ) {
                        Row {
                            Column {
                                Text(
                                    text = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(
                                        session.date
                                    ),
                                    modifier = Modifier
                                        .padding(start = 12.dp, top = 12.dp),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Location: " + session.location,
                                    modifier = Modifier
                                        .padding(start = 12.dp),
                                )
                                Text(
                                    text = "Duration: " + session.duration,
                                    modifier = Modifier
                                        .padding(start = 12.dp),
                                )
                                Text(
                                    text = "Problems Climbed: " + session.climbs.count(),
                                    modifier = Modifier
                                        .padding(start = 12.dp, bottom = 12.dp),
                                )
                            }

                            // This Spacer pushes the edit icon to the far right
                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(
                                onClick = { navController.navigate("EditSession/$index") },
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
            text = "No Sessions",
            modifier = Modifier
                .padding(12.dp),
        )
    }
}
