package com.michaelceley.android.extensions

import org.junit.Test

import org.junit.Assert.*

class ExtensionTest {

    @Test
    fun testTruncateDoesNotChangeValueIfLengthIsMax() {
        // Using a non-ascii character that is one code point in length for testing.
        val expected = "あ".repeat(20)
        val truncated = expected.utf16TruncateLength(20)

        assertEquals(expected, truncated.first)
    }

    @Test
    fun testTruncateDoesNotChangeValueIfLengthIsLessThanMax() {
        val expected = "あ".repeat(18)
        val truncated = expected.utf16TruncateLength(20)

        assertEquals(expected, truncated.first)
    }

    @Test
    fun testTruncateShortensStringToMaxLengthIfNoSurrogatePair() {
        val expected = "あ".repeat(20)
        val testValue = "あ".repeat(30)
        val truncated = testValue.utf16TruncateLength(20)

        assertEquals(expected, truncated.first)
    }

    @Test
    fun testTruncateProperlyRemovesSurrogatePairIfOnTruncationBoundary() {
        val expected = "あ".repeat(19)
        val testValue = "あ".repeat(19) + "\uD83D\uDC68"

        // Try to cut in the middle of the surrogate pair.
        val truncated = testValue.utf16TruncateLength(20)

        assertEquals(expected, truncated.first)
    }
}