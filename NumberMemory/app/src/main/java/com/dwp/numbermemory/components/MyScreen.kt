package com.dwp.numbermemory.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.repeatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.dwp.numbermemory.RandomNumberGeneratorHelper
import kotlinx.coroutines.launch

@Composable
fun MyScreen(model: NumberMemoryViewModel) {
    // create a new container that beings rendering below the digits

    // animation variables
    val maxShakeOffset = 10f
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val shakeAnimation = remember { Animatable(0f) }

    // Animate the shake effect
    suspend fun shakeScreen() {
        shakeAnimation.animateTo(
            targetValue = 1f,
            animationSpec = repeatable(
                iterations = 3,
                animation = androidx.compose.animation.core.tween(
                    durationMillis = 50,
                ),
                repeatMode = RepeatMode.Reverse
            ),
        ).also {
            // when complete reset to 0
            shakeAnimation.snapTo(0f)
        }
    }
    suspend fun stopShakeScreen() {
        shakeAnimation.snapTo(0f)
    }

    val shakeModifier = Modifier.drawWithContent {
        val shakeOffsetX = shakeAnimation.value * maxShakeOffset * density.density
        translate(left = shakeOffsetX) {
            this@drawWithContent.drawContent()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().then(shakeModifier),
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
        if (model.digitsVisibility) {
            HorizontalLayoutOfDigitTiles(digits = model.digits)
        } else {
            HorizontalLayoutOfDigitTiles(digits = model.inputs)
        }

        Box(
          modifier = Modifier.height(50.dp)
        )

        val haptic = LocalHapticFeedback.current

        NumberPadComponent(onKeyPressed = {
            digit ->
            if (digit < 0) {
                model.clearInputs()
                return@NumberPadComponent
            }
            model.addInput(digit)
            if (model.inputs.size == 1) model.digitsVisibility = false;
            if (model.inputs.size == model.digitSize) {
                val userInput = model.inputs.joinToString("")
                model.clearInputs()
                // does the input match the digits?
                val digits = model.digits.joinToString("")
                if (userInput == digits) {
                    val newDigits =
                        RandomNumberGeneratorHelper().getRandomString(model.digitSize)
                            .map { it.toString().toInt() }
                    model.updateDigits(newDigits)
                    model.setTryAgainVisible(visible = false)
                    model.incrementCorrect()

                    // play a tone
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                } else {
                    model.setTryAgainVisible(visible = true)
                    scope.launch {
                        // play a animation that shakes the entire screen
                        shakeScreen()
                    }

                }
                model.digitsVisibility = true
            }
        })

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = { model.clearInputs() },
            ) {
                Text(
                    text = "Clear",
                )
            }
            // button to regenerate the digits
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    val newDigits =
                        RandomNumberGeneratorHelper().getRandomString(model.digitSize)
                            .map { it.toString().toInt() }
                    model.updateDigits(newDigits)
                    model.clearInputs()
                    model.incrementIncorrect()
                    model.digitsVisibility = true
                    scope.launch {
                        // stop the shake
                        stopShakeScreen()
                    }
                }
            ) {
                Text(
                    text = "Try Again!",
                )
            }
        }
    }
}