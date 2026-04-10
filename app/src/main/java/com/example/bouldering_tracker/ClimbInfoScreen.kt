package com.example.bouldering_tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ClimbInfoScreen(sessionsData:List<Session>, sessionIndex: Int, climbIndex: Int, navController: NavHostController, modifier:Modifier = Modifier){
    Column (modifier=
        modifier.padding(16.dp)//add padding all around
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null,
            modifier = Modifier
                .clickable(
                    onClick = {
                        navController.popBackStack()
                    }))
        Text(
            text = "Climb Info",
            modifier = modifier.padding(bottom = 12.dp)
                .fillMaxWidth(1f),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
        )
        // Grade
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Grade: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "V" + sessionsData[sessionIndex].climbs[climbIndex].grade,
            )
        }
        // Colour
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Colour: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].climbs[climbIndex].colour,
            )
        }
        // Location
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Location: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].location,
            )
        }
        // Date
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Date: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].date,
            )
        }
        // Attempts
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Attempts: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].climbs[climbIndex].attempts.toString(),
            )
        }
        // Status
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Status: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].climbs[climbIndex].status.name,
            )
        }
        // Notes
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Notes: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].climbs[climbIndex].note,
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
    }
}