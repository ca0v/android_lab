package com.dwp.numbermemory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NumPadKeyComponent(
    contentDigit: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick(contentDigit)},
        modifier = modifier
    ) {
        DigitTileComponent(contentDigit = contentDigit)
    }
}
@Composable
fun NumberPadComponent(
    onKeyPressed: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // render a number pad, 7,8,9 in the top row, 4,5,6 in the middle row and 1,2,3 in the bottom row
    // create a grid layout
    Column(
        modifier = modifier.fillMaxWidth().padding(2.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NumPadKeyComponent(contentDigit = 7, onClick = onKeyPressed)
            NumPadKeyComponent(contentDigit = 8, onClick = onKeyPressed)
            NumPadKeyComponent(contentDigit = 9, onClick = onKeyPressed)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NumPadKeyComponent(contentDigit = 4, onClick = onKeyPressed)
            NumPadKeyComponent(contentDigit = 5, onClick = onKeyPressed)
            NumPadKeyComponent(contentDigit = 6, onClick = onKeyPressed)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NumPadKeyComponent(contentDigit = 1, onClick = onKeyPressed)
            NumPadKeyComponent(contentDigit = 2, onClick = onKeyPressed)
            NumPadKeyComponent(contentDigit = 3, onClick = onKeyPressed)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NumPadKeyComponent(contentDigit = 0, onClick = onKeyPressed)
        }
    }

}