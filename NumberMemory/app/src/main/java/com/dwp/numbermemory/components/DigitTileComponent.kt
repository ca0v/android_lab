package com.dwp.numbermemory.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NumberMemoryViewModel {
    var digits: List<Int> by mutableStateOf(emptyList())
        private set

    var tryAgainVisibility by mutableStateOf(false)
        private set

    fun updateDigits(newDigits: List<Int>) {
        digits = newDigits
    }

    fun setTryAgainVisible(visible: Boolean) {
        tryAgainVisibility = visible
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

