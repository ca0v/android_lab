package com.dwp.numbermemory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HorizontalLayoutOfDigitTiles(
    digits: List<Int>,
    modifier: Modifier = Modifier
) {
    // create a container that fills the width of the screen
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (digit in digits) {
            DigitTileComponent(contentDigit = digit)
        }
    }
}