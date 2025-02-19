package com.dwp.numbermemory.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NumberMemoryViewModel {
    var inputs: List<Int> by mutableStateOf(emptyList())
        private set

    var digits: List<Int> by mutableStateOf(emptyList())
        private set

    var tryAgainVisibility by mutableStateOf(false)
        private set

    var totalCorrect by mutableIntStateOf(0)
        private set

    var totalIncorrect by mutableIntStateOf(0)
        private set

    fun updateDigits(newDigits: List<Int>) {
        digits = newDigits
    }

    fun addInput(digit: Int) {
        inputs = inputs + digit
    }

    fun clearInputs() {
        inputs = emptyList()
    }

    fun setTryAgainVisible(visible: Boolean) {
        tryAgainVisibility = visible
    }

    fun incrementCorrect() {
        totalCorrect++
    }

    fun incrementIncorrect() {
        totalIncorrect++
    }
}