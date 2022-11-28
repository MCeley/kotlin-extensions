package com.michaelceley.android.extensions

fun String.utf16TruncateLength(
    maxLength: Int
) : Pair<String, Boolean> {

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

fun String.utf16SafeSubstring(
    startIndex: Int,
    includeSurrogateCharStart: Boolean = true
) : String {
    return utf16SafeSubstring(
        startIndex,
        length,
        includeSurrogateCharStart
    )
}

fun String.utf16SafeSubstring(
    range: IntRange,
    includeSurrogateCharStart: Boolean = true,
    includeSurrogateCharEnd: Boolean = true
) : String {
    return utf16SafeSubstring(
        range.first,
        range.last + 1,
        includeSurrogateCharStart,
        includeSurrogateCharEnd
    )
}

fun String.utf16SafeSubstring(
    startIndex: Int,
    endIndex: Int,
    includeSurrogateCharStart: Boolean = true,
    includeSurrogateCharEnd: Boolean = true
) : String {

    if(startIndex == 0 && endIndex == length) {
        // This is just the entire string with nothing cut.
        return this
    } else if(startIndex < 0 || startIndex > length) {
        // Index is out of bounds, regardless of how we cut it.
        throw StringIndexOutOfBoundsException("Index $startIndex not valid in string of length $length")
    } else if(endIndex < startIndex) {
        // Index is out of bounds, regardless of how we cut it.
        throw StringIndexOutOfBoundsException("End index {$endIndex} cannot be at or before start index {$startIndex}")
    } else if(endIndex > length) {
        // Index is out of bounds, regardless of how we cut it.
        throw StringIndexOutOfBoundsException("End index {$endIndex} not valid in string of length $length")
    } else if(startIndex == endIndex) {
        // Cuts string to length 0.
        return ""
    } else if(startIndex == 0) {
        // At this point, endIndex is not at the end, but start is at beginning.
        // Use truncation function to generate substring.
        val truncated = utf16TruncateLength(endIndex)
        return if(truncated.second && truncated.first.length != endIndex) {
            // If we cut off a surrogate pair, try again one index after.
            if(includeSurrogateCharEnd) {
                utf16TruncateLength(endIndex + 1).first
            } else {
                truncated.first
            }
        } else {
            truncated.first
        }
    }

    // Go to the last valid index before the start point and add one code point to it.
    val startIndexAfterLastValidCodePoint = this.offsetByCodePoints(startIndex - 1, 1)

    // If adding one code point to the last index before `startIndex` jumps the index
    // by two or more, `startIndex` is in the middle of a surrogate pair.
    //
    // Move the start index based on whether or not it's on a code point boundary
    // and whether or not we should keep the cut character.
    val newStartIndex = if(startIndexAfterLastValidCodePoint >= startIndex + 1) {
        if(includeSurrogateCharStart) {
            startIndex - 1
        } else {
            startIndex + 1
        }
    } else {
        startIndex
    }

    // If `endIndex` is at the end of the string, don't try to roll it forward
    // by one code point or we'll get an out of bounds.
    val newEndIndex = if(endIndex == length) {
        endIndex
    } else {
        // Go to the last valid index before the end point and add one code point to it.
        val endIndexAfterLastValidCodePoint = offsetByCodePoints(endIndex - 1, 1)

        // If adding one code point to the last index before `endIndex` jumps the index
        // by two or more, `endIndex` is in the middle of a surrogate pair.
        //
        // Move the end index based on whether or not it's on a code point boundary
        // and whether or not we should keep the cut character.
        if(endIndexAfterLastValidCodePoint >= endIndex + 1) {
            if(includeSurrogateCharEnd) {
                endIndex + 1
            } else {
                endIndex - 1
            }
        } else {
            endIndex
        }
    }

    // Return the string with the new boundaries.
    return substring(newStartIndex, newEndIndex)
}