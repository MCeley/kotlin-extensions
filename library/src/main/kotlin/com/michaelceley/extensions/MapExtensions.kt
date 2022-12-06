package com.michaelceley.extensions

/**
 * Transforms a map with string keys so that all keys are less than
 * or equal to the specified length. Entries can either be removed from
 * the map if their key is too long or the key can be truncated.
 *
 * WARNING: Truncating keys can cause key collisions and dropped values.
 *
 * @param keyLength The max length of the entry key.
 * @param truncate Whether or not keys should be truncated. If not truncated,
 *  then entries with keys that exceed the keyLength will be removed.
 * @return The filtered [Map].
 */
fun Map<String, *>.filterKeyLength(
    keyLength: Int,
    truncate: Boolean = false
) : Map<String, *> {
    return if(truncate) {
        // If truncating, map the keys to their truncated values.
        mapKeys {
            if(it.key.length > keyLength) {
                it.key.utf16TruncateLength(keyLength).first
            } else {
                it.key
            }
        }
    } else {
        // If not truncating, filter out all entries with keys exceeding keyLength.
        filterKeys {
            it.length <= keyLength
        }
    }
}