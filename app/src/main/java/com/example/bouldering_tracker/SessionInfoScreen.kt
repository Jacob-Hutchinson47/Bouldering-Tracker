package com.example.bouldering_tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
fun SessionInfoScreen(sessionsData:List<Session>, itemIndex: Int, navController: NavHostController, modifier:Modifier = Modifier){
    Column (modifier=
        modifier.padding(16.dp)//add padding all around
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null,
            modifier = Modifier
                .clickable(
                    onClick = {
                        navController.navigate(route = "Home")
                    }))
        Text(
            text = "Session Info",
            modifier = modifier.padding(bottom = 12.dp)
                .fillMaxWidth(1f),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
        )
        // Location
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Location: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[itemIndex].location,
            )
        }
        // Date
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Date: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[itemIndex].date,
            )
        }
        // Duration
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Duration: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[itemIndex].duration,
            )
        }
        // Climbs Attempted
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Climbs Attempted: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[itemIndex].climbs.count().toString(),
            )
        }
        // Climbs Sent
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Climbs Sent: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[itemIndex].climbs.count {it.status == ClimbStatus.Sent || it.status == ClimbStatus.Flashed}.toString(),
            )
        }
        // Climbs Flashed
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Climbs Flashed: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[itemIndex].climbs.count {it.status == ClimbStatus.Flashed}.toString(),
            )
        }
        Button(
            onClick = {
                //TODO
            }) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
            Text(text = " Share",
                textAlign = TextAlign.Center)
        }
        Text(
            text = "Climbs:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        ClimbsList(sessionsData[itemIndex].climbs, navController, modifier)
    }

}

@Composable
fun ClimbsList(climbs:List<Climb>, navController: NavHostController, modifier: Modifier){//create a lazy list of texts from the data
    if (climbs.count() > 0) {
        LazyColumn {
            itemsIndexed(climbs) {//iterate through each climb in the List and create a Card for each climb
                    index, climb ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(4.dp).fillMaxWidth(1f)
                        .clickable(
                            onClick = { //handle the onClick event to the list item
                                navController.navigate(route = "ClimbInfo/$index")
                            })
                ) {
                    Text(
                        text = "V" + climb.grade + " - " + climb.colour + " Holds",
                        modifier = Modifier
                            .padding(start = 12.dp, top = 12.dp),
                    )
                    Text(
                        text = "Attempts: " + climb.attempts,
                        modifier = Modifier
                            .padding(start = 12.dp),
                    )
                    Text(
                        text = "Status: " + climb.status.name,
                        modifier = Modifier
                            .padding(start = 12.dp, bottom = 12.dp),
                    )
                }
            }
        }
    } else {
        Text(
            text = "No Climbs",
            modifier = Modifier
                .padding(12.dp),
        )
    }
}
