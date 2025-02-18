package com.dwp.numbermemory

/*
 * Generates a random number of any length that has no repeating neighboring digits
 */
interface IRandomNumberGenerator {
    // function for generating a random digit
    fun getRandomString(length: Int): String {
        return RandomNumberGeneratorHelper().getRandomString(length)
    }
}