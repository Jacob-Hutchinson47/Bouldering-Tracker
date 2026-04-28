package com.example.bouldering_tracker.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bouldering_tracker.AppScreens
import com.example.bouldering_tracker.data.Climb
import com.example.bouldering_tracker.data.ClimbStatus
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SessionInfoScreen(viewModel: SessionViewModel, sessionIndex: Int, navController: NavHostController, modifier:Modifier = Modifier){
    val sessionsData by viewModel.sessionsData.observeAsState(initial = emptyList())

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
                text = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(sessionsData[sessionIndex].date),
            )
        }
        // Duration
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Duration: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].duration,
            )
        }
        // Climbs Attempted
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Climbs Attempted: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].climbs.count().toString(),
            )
        }
        // Climbs Sent
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Climbs Sent: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].climbs.count {it.status == ClimbStatus.Sent || it.status == ClimbStatus.Flashed}.toString(),
            )
        }
        // Climbs Flashed
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Climbs Flashed: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsData[sessionIndex].climbs.count {it.status == ClimbStatus.Flashed}.toString(),
            )
        }
        // Highest Grade Sent
        Row(modifier =  Modifier.padding(8.dp)){
            Text(
                text = "Highest Grade Sent: ",
                fontWeight = FontWeight.Bold,
            )
            val highestGrade = sessionsData[sessionIndex].climbs
                .filter { it.status == ClimbStatus.Sent || it.status == ClimbStatus.Flashed }
                .maxOfOrNull {it.grade}
            Text(
                text = if (highestGrade != null) "V$highestGrade" else "N/A"
            )
        }
        Row () {
            Button( // Share
                onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        val shareText = "I just finished a bouldering session at ${sessionsData[sessionIndex].location}! " +
                                "I attempted ${sessionsData[sessionIndex].climbs.size} climbs."
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share Session via")
                    context.startActivity(shareIntent)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
                Text(
                    text = " Share",
                    textAlign = TextAlign.Center
                )
            }
            Button( // View on map
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
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                Text("View on Map")
            }
        }
        Text(
            text = "Climbs:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        ClimbsList(sessionsData[sessionIndex].climbs, sessionIndex, navController)
    }
}

@Composable
fun ClimbsList(
    climbs: List<Climb>,
    sessionIndex: Int, // Add this parameter
    navController: NavHostController,
) {
    if (climbs.count() > 0) {
        LazyColumn {
            itemsIndexed(climbs) {//iterate through each climb in the List and create a Card for each climb
                    climbIndex, climb ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(4.dp).fillMaxWidth(1f)
                        .clickable(
                            onClick = { //handle the onClick event to the list item
                                navController.navigate("${AppScreens.ClimbInfo.name}/$sessionIndex/$climbIndex")
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
