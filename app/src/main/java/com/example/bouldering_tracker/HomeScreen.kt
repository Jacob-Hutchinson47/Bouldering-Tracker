package com.example.bouldering_tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(sessionsData: List<Session>, navController: NavHostController, modifier: Modifier = Modifier) {
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
        SessionsList(sessionsData, navController, modifier)
    }
}

@Composable
fun SessionsList(sessions:List<Session>, navController: NavHostController, modifier: Modifier){//create a lazy list of texts from the data
    if (sessions.count() > 0) {
        LazyColumn {
            itemsIndexed(sessions) {//iterate through each session in the List and create a Card for each session
                    sessionIndex, session ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(4.dp).fillMaxWidth(1f)
                        .clickable(
                            onClick = { //handle the onClick event to the list item
                                navController.navigate(route = "SessionInfo/$sessionIndex")
                            })
                ) {
                    Text(
                        text = session.date,
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
