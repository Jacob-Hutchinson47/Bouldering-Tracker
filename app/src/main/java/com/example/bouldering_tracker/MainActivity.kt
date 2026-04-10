package com.example.bouldering_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bouldering_tracker.ui.theme.BoulderingTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoulderingTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController: NavHostController = rememberNavController()
                        val sessionsData: List<Session> = listOf(
                            Session("The Climbing Station","13/03/26", "1h 45m", listOf<Climb>()),
                            Session("The Climbing Station","8/03/26", "2h", listOf<Climb>()),
                            Session("The Climbing Station","28/02/26", "1h50m", listOf<Climb>()),
                            Session("The Climbing Station","25/02/26", "1h20m", listOf<Climb>()),
                            Session("The Climbing Station","4/02/26", "1h50m", listOf<Climb>()),
                        )
                        NavHost(
                            navController = navController,
                            startDestination = com.example.bouldering_tracker.AppScreens.Home.name
                        ){
                            //call the composable() function once for each of the routes
                            composable(route = com.example.bouldering_tracker.AppScreens.Home.name){
                                HomeScreen(sessionsData, navController)
                            }
                            composable(
                                route = com.example.bouldering_tracker.AppScreens.SessionInfo.name+"/{index}",
                                arguments = listOf(
                                    navArgument(name = "index"){
                                        type = NavType.IntType //extract the argument
                                    }
                                )
                            ) {
                                    index->
                                SessionInfoScreen(
                                    sessionsData,
                                    itemIndex = index.arguments?.getInt("index")?:0//passing the index
                                )
                            }

                        }
                    }
                }
            }
        }
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