package com.michaelceley.extensions

import kotlin.test.Test
import kotlin.test.assertEquals

class MapExtensionTests {

    @Test
    fun `filterKeyLength can be called on empty map without exception`() {
        val expectedValue = mapOf<String, Any>()
        val actualValue = expectedValue.filterKeyLength(10)

        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Keys of max length are not dropped when not truncating`() {
        val expectedValue = mapOf(
            "1234567890" to ""
        )
        val actualValue = expectedValue.filterKeyLength(10, false)
    }

    @Test
    fun `Keys of max length are not altered when truncating`() {
        val expectedValue = mapOf(
            "1234567890" to ""
        )
        val actualValue = expectedValue.filterKeyLength(10, true)
    }

    @Test
    fun `Keys exceeding max length are dropped when not truncating`() {
        val expectedValue = mapOf(
            "short key" to "",
            "shorter" to ""
        )
        val actualValue = mapOf(
            "this is a long key" to "",
            "short key" to "",
            "this is a longer key" to "",
            "shorter" to ""
        ).filterKeyLength(10, false)

        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Keys exceeding max length are truncated`() {
        val expectedValue = mapOf(
            "long key o" to "",
            "short key" to "",
            "this is a " to "",
            "shorter" to ""
        )
        val actualValue = mapOf(
            "long key of length exceeding 10" to "",
            "short key" to "",
            "this is a longer key" to "",
            "shorter" to ""
        ).filterKeyLength(10, true)

        assertEquals(expectedValue, actualValue)
        assertEquals(4, actualValue.size)
    }

    @Test
    fun `Keys exceeding max length are truncated with colliding keys overwritten`() {
        val expectedValue = mapOf(
            "short key" to "",
            "this is a " to "overwritten value",
            "shorter" to ""
        )
        val actualValue = mapOf(
            "this is a long key" to "dropped value",
            "short key" to "",
            "this is a longer key" to "overwritten value",
            "shorter" to ""
        ).filterKeyLength(10, true)

        assertEquals(expectedValue, actualValue)
        assertEquals(3, actualValue.size)
        assertEquals("overwritten value", actualValue["this is a "])
    }
}