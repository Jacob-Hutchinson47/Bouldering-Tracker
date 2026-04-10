package com.example.bouldering_tracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SessionInfoScreen(sessionsData:List<List<String>>, itemIndex: Int, modifier:Modifier = Modifier){
    Text(
        text = sessionsData[itemIndex][0],
        textAlign = TextAlign.Center,
        fontSize = 30.sp,
        modifier = modifier.padding(16.dp)
    )
}