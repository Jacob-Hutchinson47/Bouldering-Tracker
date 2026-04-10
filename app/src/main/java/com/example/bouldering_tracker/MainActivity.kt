package com.example.bouldering_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bouldering_tracker.ui.theme.BoulderingTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoulderingTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(//Let Surface take care of the innerPadding in Scaffold, place the WeatherSearchScreen in the correct place, and use theme colours, etc.
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HomeScreen()//uses an empty modifier of its own
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current //get the activity context within a composable function, use "this" instead in an activity
    val data: List<List<String>> = listOf(listOf("*Date*", "*Location*", "*Time*", "*Climbs*"),
        listOf("13/03/26", "The Climbing Station", "1h 45m", "15"),
        listOf("8/03/26", "The Climbing Station", "2h", "22"),
        listOf("28/02/26", "The Climbing Station", "1h50m", "19"),
        listOf("25/02/26", "The Climbing Station", "1h20m", "11"),
        listOf("4/02/26", "The Climbing Station", "1h50m", "19"))
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
            Button(
                onClick = {
                    //TODO
                },
                modifier
                    .weight(1f, true)
                    .height(64.dp),
            ) {
                Text(text = "Create New Session",
                    textAlign = TextAlign.Center)
            }
            Button(
                onClick = {
                    //TODO
                },
                modifier
                    .weight(1f, true)
                    .height(64.dp)
            ) {
                Text(text = "View Stats",
                    textAlign = TextAlign.Center)
            }
            Button(
                onClick = {
                    //TODO
                },
                modifier
                    .weight(1f, true)
                    .height(64.dp)
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
        SessionsList(data,modifier)
    }
}

@Composable
fun SessionsList(data:List<List<String>>, modifier: Modifier){//create a lazy list of texts from the dummy data
    LazyColumn {
        items(data) {//iterate through each item in the List and create a Text for each item
                item ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .padding(4.dp).fillMaxWidth(1f)
            ) {
                Text(
                    text = item[0],
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Location: " + item[1],
                    modifier = Modifier
                        .padding(start = 12.dp),
                )
                Text(
                    text = "Duration: " + item[2],
                    modifier = Modifier
                        .padding(start = 12.dp),
                )
                Text(
                    text = "Problems Sent: " + item[3],
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 12.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BoulderingTrackerTheme {
        HomeScreen()
    }
}

enum class AppScreens{
    Home, //AppScreens.Home.name
    SessionInfo, //AppScreens.SessionInfo.name
    ClimbInfo, //AppScreens.ClimbInfo.name
    CreateSession, //AppScreens.CreateSession.name
    AddClimb, //AppScreens.AddClimb.name
    ViewStats, //AppScreens.ViewStats.name
    Settings, //AppScreens.Settings.name

}