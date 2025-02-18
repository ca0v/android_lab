package com.dwp.numbermemory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dwp.numbermemory.RandomNumberGeneratorHelper

@Composable
fun MyScreen(model: NumberMemoryViewModel) {
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
                val userInput = input.toString()
                input.clear()
                // does the input match the digits?
                val digits = model.digits.joinToString("");
                if (userInput == digits) {
                    val newDigits =
                        RandomNumberGeneratorHelper().getRandomString(6)
                            .map { it.toString().toInt() }
                    model.updateDigits(newDigits)
                    model.setTryAgainVisible(visible = false)
                } else {
                    model.setTryAgainVisible(visible = true)
                }
            }
        })

        if (model.tryAgainVisibility) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // button to regenerate the digits
            Button(
                onClick = {
                    val newDigits =
                        RandomNumberGeneratorHelper().getRandomString(6)
                            .map { it.toString().toInt() }
                    model.updateDigits(newDigits)
                    model.setTryAgainVisible(visible = false)
                }
            ) {
                Text(
                    text = "Try Again!",
                )
            }
        }
        }
    }
}