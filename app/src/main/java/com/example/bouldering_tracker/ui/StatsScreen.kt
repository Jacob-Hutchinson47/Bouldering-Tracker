package com.example.bouldering_tracker.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bouldering_tracker.data.ClimbStatus

@Composable
fun StatsScreen(viewModel: SessionViewModel, navController: NavHostController, modifier:Modifier = Modifier){
    val sessionsData by viewModel.sessionsData.observeAsState(initial = emptyList())

    val context = LocalContext.current

    val favoriteLocationEntry = sessionsData.groupBy { it.location }.maxByOrNull { it.value.size }
    val favoriteLocation = favoriteLocationEntry?.key ?: "N/A"
    val sessionsAtFavoriteCount = favoriteLocationEntry?.value?.size ?: 0

    val allClimbs = sessionsData.flatMap { it.climbs }

    val highestGradeCompleted = allClimbs
        .filter { it.status == ClimbStatus.Sent || it.status == ClimbStatus.Flashed }
        .maxOfOrNull { it.grade }

    val countOfHighestGrade = allClimbs.count {
        it.grade == highestGradeCompleted && (it.status == ClimbStatus.Sent || it.status == ClimbStatus.Flashed)
    }

    val highestGradeAttempted = allClimbs.maxOfOrNull { it.grade }
    val totalClimbs = allClimbs.size
    val totalFlashes = allClimbs.count { it.status == ClimbStatus.Flashed }

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
            text = "Statistics",
            modifier = modifier.padding(bottom = 12.dp)
                .fillMaxWidth(1f),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
        )

        // Favorite Location
        Row(modifier =  Modifier.padding(4.dp)){
            Text(
                text = "Favorite Location: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = favoriteLocation,
            )
        }
        // Sessions at Favorite Location
        Row(modifier =  Modifier.padding(4.dp)){
            Text(
                text = "Sessions at Favorite Location: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sessionsAtFavoriteCount.toString(),
            )
        }

        // Highest Grade Completed
        Row(modifier
            .padding(4.dp)
            .padding(top = 26.dp)){
            Text(
                text = "Highest Grade Completed: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = if (highestGradeCompleted != null) "V$highestGradeCompleted" else "N/A"
            )
        }
        // Number of Highest Grade Completed
        Row(modifier
            .padding(4.dp)){
            Text(
                text = "Number of Highest Grade Completed: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = countOfHighestGrade.toString()
            )
        }
        // Highest Grade Attempted
        Row(modifier
            .padding(4.dp)){
            Text(
                text = "Highest Grade Attempted: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = if (highestGradeAttempted != null) "V$highestGradeAttempted" else "N/A"
            )
        }
        // Total Climbs
        Row(modifier
            .padding(4.dp)
            .padding(top = 26.dp)){
            Text(
                text = "Total Climbs: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = totalClimbs.toString()
            )
        }
        // Total Flashes
        Row(modifier
            .padding(4.dp)){
            Text(
                text = "Total Flashes: ",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = totalFlashes.toString()
            )
        }
        Button( // Share
            onClick = {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    val shareText = "Here are my climbing stats: " +
                            "My favorite location is ${favoriteLocation} with ${sessionsAtFavoriteCount} visits. " +
                            "The highest grade I have completed is V${highestGradeCompleted}. " +
                            "I have done ${totalClimbs} total climbs!"
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
        Text(
            text = "Grades Completed Distribution:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        // 1. Get all climbs that were completed
        val completedClimbs = sessionsData.flatMap { it.climbs }.filter {
            it.status == ClimbStatus.Sent || it.status == ClimbStatus.Flashed
        }

        // 2. Group these climbs by grade and count them
        val gradeCounts = completedClimbs.groupBy { it.grade }
            .mapValues { it.value.size }

        // 3. Find the highest count among completed climbs to scale the bar widths
        val maxCount = gradeCounts.values.maxOfOrNull { it } ?: 1

        // 4. Sort the grades for the display
        val sortedGrades = gradeCounts.keys.sorted()

        Column(modifier = Modifier.padding(start = 8.dp)) {
            if (sortedGrades.isEmpty()) {
                Text("No data available", fontSize = 14.sp, color = Color.Gray)
            } else {
                sortedGrades.forEach { grade ->
                    GradeBar(
                        grade = grade,
                        count = gradeCounts[grade] ?: 0,
                        maxCount = maxCount

                    )
                }
            }
        }
    }
}

@Composable
fun GradeBar(grade: Int, count: Int, maxCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "V$grade",
            modifier = Modifier.width(40.dp),
            fontWeight = FontWeight.Medium
        )

        // The Bar
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = (count.toFloat() / maxCount) * 0.8f) // Scale bar based on max
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xff7891d7)) // Light blue from your image
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)) // Optional border
        )
    }
}