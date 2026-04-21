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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bouldering_tracker.ui.AddClimbScreen
import com.example.bouldering_tracker.ui.ClimbInfoScreen
import com.example.bouldering_tracker.ui.CreateSessionScreen
import com.example.bouldering_tracker.ui.HomeScreen
import com.example.bouldering_tracker.ui.SessionInfoScreen
import com.example.bouldering_tracker.ui.SessionViewModel
import com.example.bouldering_tracker.ui.SettingsScreen
import com.example.bouldering_tracker.ui.StatsScreen
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
                        val viewModel: SessionViewModel = viewModel()

                        NavHost(
                            navController = navController,
                            startDestination = com.example.bouldering_tracker.AppScreens.Home.name
                        ){
                            //call the composable() function once for each of the routes
                            composable(route = com.example.bouldering_tracker.AppScreens.Home.name){
                                HomeScreen(viewModel, navController)
                            }
                            composable(
                                route = AppScreens.SessionInfo.name + "/{sessionIndex}",
                                arguments = listOf(navArgument("sessionIndex") { type = NavType.IntType })
                            ) { backStackEntry ->
                                SessionInfoScreen(
                                    viewModel,
                                    sessionIndex = backStackEntry.arguments?.getInt("sessionIndex")
                                        ?: 0,
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
                                    viewModel,
                                    sessionIndex = sessionIndex,
                                    climbIndex = climbIndex,
                                    navController
                                )
                            }
                            composable(route = com.example.bouldering_tracker.AppScreens.Stats.name){
                                StatsScreen(viewModel, navController)
                            }
                            composable(route = com.example.bouldering_tracker.AppScreens.Settings.name){
                                SettingsScreen(navController)
                            }
                            composable(route = com.example.bouldering_tracker.AppScreens.CreateSession.name){
                                CreateSessionScreen(viewModel, navController)
                            }
                            composable(
                                route = "${AppScreens.AddClimb.name}/{sessionIndex}",
                                arguments = listOf(navArgument("sessionIndex") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val sessionIndex = backStackEntry.arguments?.getInt("sessionIndex") ?: 0
                                AddClimbScreen(viewModel, sessionIndex, navController)
                            }
                            /*composable(
                                route = AppScreens.EditClimb.name + "/{climbIndex}",
                                arguments = listOf(navArgument("climbIndex") { type = NavType.IntType })
                            ) { backStackEntry ->
                                EditClimbScreen(
                                    climbsData,
                                    climbIndex = backStackEntry.arguments?.getInt("climbIndex") ?: 0,
                                    navController
                                )
                            }*/
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
    EditSession, //AppScreens.EditSession.name
    AddClimb, //AppScreens.AddClimb.name
    Stats, //AppScreens.Stats.name
    Settings, //AppScreens.Settings.name
}