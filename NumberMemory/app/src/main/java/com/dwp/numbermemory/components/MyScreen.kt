package com.dwp.numbermemory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dwp.numbermemory.RandomNumberGeneratorHelper

@Composable
fun MyScreen(model: NumberMemoryViewModel) {
    // create a new container that beings rendering below the digits
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // create a buffer area above the digits
        Row(
            // empty space 10p tall
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().height(100.dp)
        ) {
        }

        ScoreBoardComponent(model.totalCorrect, model.totalIncorrect)

        // flow digits that can be modified to update the UI
        HorizontalLayoutOfDigitTiles(digits = model.digits)

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().height(80.dp)
        ) {
            HorizontalLayoutOfDigitTiles(digits = model.inputs)
        }

        NumberPadComponent(onKeyPressed = {
            digit -> model.addInput(digit)
            if (model.inputs.size == 6) {
                val userInput = model.inputs.joinToString("")
                model.clearInputs()
                // does the input match the digits?
                val digits = model.digits.joinToString("")
                if (userInput == digits) {
                    val newDigits =
                        RandomNumberGeneratorHelper().getRandomString(6)
                            .map { it.toString().toInt() }
                    model.updateDigits(newDigits)
                    model.setTryAgainVisible(visible = false)
                    model.incrementCorrect()
                } else {
                    model.setTryAgainVisible(visible = true)
                    model.incrementIncorrect()
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