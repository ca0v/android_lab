package com.dwp.numbermemory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScoreBoardComponent(
    totalCorrect: Int,
    totalIncorrect: Int,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$totalCorrect",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .clip(CircleShape)
                .background(androidx.compose.ui.graphics.Color.Green)
                .padding(10.dp)
                .width(50.dp)
                .height(50.dp)
                .wrapContentSize(Alignment.Center)
        )


        if (totalIncorrect > 0) {
            Text(
                text = "$totalIncorrect",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color.Red)
                    .padding(10.dp)
                    .width(50.dp)
                    .height(50.dp)
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}