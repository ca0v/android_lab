package com.dwp.numbermemory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.dwp.numbermemory.RandomNumberGeneratorHelper

class NumberMemoryViewModel {
    var digits: List<Int> by mutableStateOf(emptyList())
        private set

    fun updateDigits(newDigits: List<Int>) {
        digits = newDigits
    }
}
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

@Composable
fun MyScreen() {
    val model = NumberMemoryViewModel()
    val randomDigits = RandomNumberGeneratorHelper().getRandomString(6)
    val digits = randomDigits.map { it.toString().toInt() }
    model.updateDigits(digits);

    // create a new container that beings rendering below the digits
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // flow digits that can be modified to update the UI
        HorizontalLayoutOfDigitTiles(digits = model.digits)

        // button to regenerate the digits
        Button(
            onClick = {
                val newDigits =
                    RandomNumberGeneratorHelper().getRandomString(6).map { it.toString().toInt() }
                model.updateDigits(newDigits)
            }
        ) { }
    }
}