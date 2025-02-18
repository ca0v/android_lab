package com.dwp.numbermemory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dwp.numbermemory.RandomNumberGeneratorHelper

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

        val input = StringBuilder()
        NumberPadComponent(onKeyPressed = {
            digit -> input.append(digit)
            if (input.length == 6) {
                // does the input match the digits?
                if (input.toString() == randomDigits) {
                    val newDigits =
                        RandomNumberGeneratorHelper().getRandomString(6)
                            .map { it.toString().toInt() }
                    model.updateDigits(newDigits)
                } else {
                    println("Incorrect input")
                }
                input.clear()
            }
        })

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