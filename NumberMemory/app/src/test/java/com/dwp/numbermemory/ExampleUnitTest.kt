package com.dwp.numbermemory

import org.junit.Test

import org.junit.Assert.*
import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun random_number_hasSixDigits() {
        // call IRandomNumberGenerator.getRandomString(6)
        val randomNumber = RandomNumberGeneratorHelper().getRandomString(6)
        // check that the string has six digits
        assertEquals(6, randomNumber.toString().length)
        // no neighbors should have the same digit
        for (i in 0 until randomNumber.length - 1) {
            assertNotEquals(randomNumber[i], randomNumber[i + 1])
        }

    }
}