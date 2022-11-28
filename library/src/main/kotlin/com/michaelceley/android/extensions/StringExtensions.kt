package com.michaelceley.android.extensions

fun String.utf16TruncateLength(maxLength: Int) : Pair<String, Boolean> {

    // If the value of this is under maxLength units, return unchanged value.
    if(length <= maxLength) {
        return this to false
    }

    // Go to the last valid index and add one code point to it.
    val indexAfterLastValidCodePoint = this.offsetByCodePoints(maxLength - 1, 1)

    // If adding one code point to the last valid index jumps the index by two
    // or more, it's a multi-unit character and we should drop the partial character
    // and truncate to (maxLength - 1).
    val truncatedValue = if(indexAfterLastValidCodePoint >= maxLength + 1) {
        this.substring(0, maxLength - 1)
    } else {
        this.substring(0, maxLength)
    }

    // Return the truncated value and true to denote the value has been truncated.
    return truncatedValue to true
}