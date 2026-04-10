package com.example.bouldering_tracker

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ClimbInfoScreen(sessionsData:List<Session>, sessionIndex: Int, climbIndex: Int, navController: NavHostController, modifier:Modifier = Modifier){
    val context = LocalContext.current
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
        Row() {
            Button(
                onClick = {
                    //TODO
                },
                modifier =  Modifier.padding(8.dp)) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
                Text(
                    text = " Share",
                    textAlign = TextAlign.Center
                )
            }
            Button(
                onClick = {
                    //get session location
                    val location = sessionsData[sessionIndex].location
                    //construct the uri
                    val geoUri = Uri.parse("geo:0,0?q=$location")
                    //val geoUri = ("geo:0,0?q=$location").toUri()
                    Log.d("uri", location)
                    //create an Intent
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        //set the uri data this intent is operating on
                        data = geoUri
                    }
                    //if there is an app that can handle the implicit intent
                    if (intent.resolveActivity(context.packageManager) != null) {
                        Log.d("ClimbInfoScreen", "there is an app")
                        context.startActivity(intent)
                    } else {
                        Log.d("ClimbInfoScreen", "no app handling implicit intent")
                    }
                },
                modifier =  Modifier.padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                Text("View on Map")}
        }
    }
}