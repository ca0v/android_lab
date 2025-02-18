package com.dwp.numbermemory

import kotlin.random.Random

class RandomNumberGeneratorHelper {
    fun getRandomDigit(): Int {
        val randomDigit = Random.nextInt(10)
        return randomDigit
    }

    fun getRandomDigitNotEqualTo(previousDigit: Int): Int {
        var randomDigit = getRandomDigit()
        while (randomDigit == previousDigit) {
            randomDigit = getRandomDigit()
        }
        return randomDigit
    }

    fun getRandomString(length: Int): String {
        val randomString = StringBuilder()
        val previousDigit = -1
        for (i in 1..length) {
            val randomDigit = getRandomDigitNotEqualTo(previousDigit)
            randomString.append(randomDigit)
        }
        return randomString.toString()
    }
}

