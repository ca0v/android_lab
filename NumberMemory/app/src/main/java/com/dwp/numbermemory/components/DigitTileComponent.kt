package com.dwp.numbermemory.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text

@Composable
fun DigitTileComponent(
    contentDigit: Int,
) {
    val digitString = contentDigit.toString()
    // use material theme typography to style the text
    Text(
        text = digitString,
        style = androidx.compose.material3.MaterialTheme.typography.displayLarge
    )
}

@Composable
fun ScoreBoardComponent(
    totalCorrect: Int,
    totalIncorrect: Int,
) {
    Text(
        text = "Correct: $totalCorrect, Incorrect: $totalIncorrect",
        style = androidx.compose.material3.MaterialTheme.typography.displaySmall
    )
}