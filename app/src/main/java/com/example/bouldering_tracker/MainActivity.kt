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
                            Session("The Climbing Station", "13/03/26", "1h 45m", listOf(
                                Climb(3, "Red", 2, ClimbStatus.Sent, "Just go up"),
                                Climb(4, "Green", 3, ClimbStatus.Flashed, "Don't fall off")
                            )),
                            Session("The Climbing Station", "08/03/26", "2h", emptyList()),

                            // New Data Starts Here
                            Session("Big Rock Hub", "20/03/26", "2h 15m", listOf(
                                Climb(2, "Blue", 1, ClimbStatus.Flashed, "Warm up"),
                                Climb(4, "Black", 5, ClimbStatus.Sent, "Hard crimpy move at the top"),
                                Climb(5, "Yellow", 8, ClimbStatus.Project, "Need more finger strength for the start")
                            )),
                            Session("The Depot", "27/03/26", "1h 30m", listOf(
                                Climb(3, "Purple", 2, ClimbStatus.Sent, "Nice slab"),
                                Climb(3, "Purple", 1, ClimbStatus.Flashed, "Easy dynamic move"),
                                Climb(4, "White", 4, ClimbStatus.Sent, "Technical footwork required")
                            )),
                            Session("Big Rock Hub", "02/04/26", "2h", listOf(
                                Climb(4, "Green", 2, ClimbStatus.Sent, "Repeat from last time"),
                                Climb(5, "Yellow", 10, ClimbStatus.Sent, "FINALLY SENT IT!"),
                                Climb(6, "Orange", 3, ClimbStatus.Project, "New highest grade attempt")
                            )),
                            Session("The Climbing Station", "05/04/26", "1h 10m", listOf(
                                Climb(3, "Red", 1, ClimbStatus.Flashed, "Quick session"),
                                Climb(4, "Green", 2, ClimbStatus.Sent, "Feeling strong")
                            )),
                            Session("Flashpoint", "10/04/26", "2h 30m", listOf(
                                Climb(2, "Blue", 1, ClimbStatus.Flashed, "Good reset"),
                                Climb(3, "Red", 1, ClimbStatus.Flashed, "Soft for the grade"),
                                Climb(4, "Black", 6, ClimbStatus.Sent, "Burly overhang"),
                                Climb(5, "Yellow", 4, ClimbStatus.Project, "Pumped out at the end")
                            ))
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
                                route = AppScreens.SessionInfo.name + "/{sessionIndex}",
                                arguments = listOf(navArgument("sessionIndex") { type = NavType.IntType })
                            ) { backStackEntry ->
                                SessionInfoScreen(
                                    sessionsData,
                                    sessionIndex = backStackEntry.arguments?.getInt("sessionIndex") ?: 0,
                                    navController
                                )
                            }
                            composable(
                                route = com.example.bouldering_tracker.AppScreens.ClimbInfo.name+"/{sessionIndex}/{climbIndex}",
                                arguments = listOf(
                                    navArgument(name = "sessionIndex"){
                                        type = NavType.IntType
                                    },
                                    navArgument(name = "climbIndex"){
                                        type = NavType.IntType
                                    }
                                )
                            ) {
                                    backStackEntry ->
                                val sessionIndex = backStackEntry.arguments?.getInt("sessionIndex") ?: 0
                                val climbIndex = backStackEntry.arguments?.getInt("climbIndex") ?: 0

                                ClimbInfoScreen(
                                    sessionsData,
                                    sessionIndex = sessionIndex,
                                    climbIndex = climbIndex,
                                    navController
                                )
                            }
                            composable(route = com.example.bouldering_tracker.AppScreens.Stats.name){
                                StatsScreen(sessionsData, navController)
                            }
                            composable(route = com.example.bouldering_tracker.AppScreens.Settings.name){
                                SettingsScreen(navController)
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
    Stats, //AppScreens.Stats.name
    Settings, //AppScreens.Settings.name
}