package com.michaelceley.extensions

import org.junit.Assert.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


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

    @Test
    fun testSubstringWorksOnShortStrings() {
        val expected = "a"

        var testValue = "a".utf16SafeSubstring(0)
        assertEquals(expected, testValue)

        testValue = "aa".utf16SafeSubstring(1)
        assertEquals(expected, testValue)

        // Test with indices.
        testValue = "a".utf16SafeSubstring(0, 1)
        assertEquals(expected, testValue)

        testValue = "ab".utf16SafeSubstring(0,1)
        assertEquals(expected, testValue)

        testValue = "a\uD83D\uDC68".utf16SafeSubstring(0, 2, includeSurrogateCharEnd = true)
        assertNotEquals(expected, testValue)

        testValue = "a\uD83D\uDC68".utf16SafeSubstring(0, 2, includeSurrogateCharEnd = false)
        assertEquals(expected, testValue)

        testValue = "\uD83D\uDC68a".utf16SafeSubstring(1, 3, includeSurrogateCharStart = true)
        assertNotEquals(expected, testValue)

        testValue = "\uD83D\uDC68a".utf16SafeSubstring(1, 3, includeSurrogateCharStart = false)
        assertEquals(expected, testValue)

        // Test with ranges
        testValue = "a".utf16SafeSubstring(0..0)
        assertEquals(expected, testValue)

        testValue = "ab".utf16SafeSubstring(0..0)
        assertEquals(expected, testValue)

        testValue = "a\uD83D\uDC68".utf16SafeSubstring(0..1, includeSurrogateCharEnd = true)
        assertNotEquals(expected, testValue)

        testValue = "a\uD83D\uDC68".utf16SafeSubstring(0..1, includeSurrogateCharEnd = false)
        assertEquals(expected, testValue)
    }

    @Test
    fun testSubstringProperlyIncludesSurrogatePairsWithStartIndex() {
        var expected = "a\uD83D\uDC68"
        var testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1, includeSurrogateCharStart = false
        )

        assertEquals(expected, testValue)

        expected = "\uD83D\uDC68a\uD83D\uDC68"
        testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1, includeSurrogateCharStart = true
        )
        assertEquals(expected, testValue)
    }

    @Test
    fun testSubstringProperlyIncludesSurrogatePairsWithStartAndEndIndex() {
        var expected = "a"
        var testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1, 4,
            includeSurrogateCharStart = false,
            includeSurrogateCharEnd = false
        )

        assertEquals(expected, testValue)

        expected = "\uD83D\uDC68a"
        testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1, 4,
            includeSurrogateCharStart = true,
            includeSurrogateCharEnd = false
        )
        assertEquals(expected, testValue)

        expected = "a\uD83D\uDC68"
        testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1, 4,
            includeSurrogateCharStart = false,
            includeSurrogateCharEnd = true
        )
        assertEquals(expected, testValue)

        expected = "\uD83D\uDC68a\uD83D\uDC68"
        testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1, 4,
            includeSurrogateCharStart = true,
            includeSurrogateCharEnd = true
        )
        assertEquals(expected, testValue)
    }

    @Test
    fun testSubstringProperlyIncludesSurrogatePairsWithRange() {
        var expected = "a"
        var testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1..3,
            includeSurrogateCharStart = false,
            includeSurrogateCharEnd = false
        )

        assertEquals(expected, testValue)

        expected = "\uD83D\uDC68a"
        testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1..3,
            includeSurrogateCharStart = true,
            includeSurrogateCharEnd = false
        )
        assertEquals(expected, testValue)

        expected = "a\uD83D\uDC68"
        testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1..3,
            includeSurrogateCharStart = false,
            includeSurrogateCharEnd = true
        )
        assertEquals(expected, testValue)

        expected = "\uD83D\uDC68a\uD83D\uDC68"
        testValue = "\uD83D\uDC68a\uD83D\uDC68".utf16SafeSubstring(
            1..3,
            includeSurrogateCharStart = true,
            includeSurrogateCharEnd = true
        )
        assertEquals(expected, testValue)
    }

    @Test
    fun testSubstringThrowsWhenOutOfBounds() {
        assertThrows(StringIndexOutOfBoundsException::class.java) {
            "a".utf16SafeSubstring(-1, 0)
        }

        assertThrows(StringIndexOutOfBoundsException::class.java) {
            "a".utf16SafeSubstring(0, 2)
        }

        assertThrows(StringIndexOutOfBoundsException::class.java) {
            "a".utf16SafeSubstring(1, 0)
        }

        assertThrows(StringIndexOutOfBoundsException::class.java) {
            "a".utf16SafeSubstring(10, 11)
        }
    }

    @Test
    fun testSubstringGeneratesZeroLengthValueWithMatchingIndices() {
        assertEquals("", "a".utf16SafeSubstring(1, 1))
        assertEquals("", "Testing".utf16SafeSubstring(3, 3))
    }

    @Test
    fun testSubstringFunctionsSameAsBuiltInSubstringWhenNoBoundariesHit() {
        var expected = "a little string".substring(0, 1)
        var testValue = "a little string".utf16SafeSubstring(0, 1)

        assertEquals(expected, testValue)

        expected = "a \uD83D\uDC68 string".substring(0, 6)
        testValue = "a \uD83D\uDC68 string".utf16SafeSubstring(0, 6)

        assertEquals(expected, testValue)

        expected = "a \uD83D\uDC68 string".substring(0..5)
        testValue = "a \uD83D\uDC68 string".utf16SafeSubstring(0..5)

        assertEquals(expected, testValue)

        expected = "a \uD83D\uDC68".substring(0, 4)
        testValue = "a \uD83D\uDC68".utf16SafeSubstring(0,4)

        assertEquals(expected, testValue)

        expected = "a \uD83D\uDC68".substring(0..3)
        testValue = "a \uD83D\uDC68".utf16SafeSubstring(0..3)

        assertEquals(expected, testValue)
    }
}