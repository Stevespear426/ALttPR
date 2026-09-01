package com.stingers.alttpr.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun currentTimeInMillis() = Clock.System.now().toEpochMilliseconds()

inline fun <reified T : Enum<T>> String?.toEnumOrDefault(defaultValue: T): T {
    return enumValues<T>().firstOrNull { it.name.equals(this, ignoreCase = true) } ?: defaultValue
}

inline fun <reified T : Enum<T>> String?.toEnumOrNull(): T? {
    return enumValues<T>().firstOrNull { it.name.equals(this, ignoreCase = true) }
}


inline fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
        )
    }
}


inline fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
        flow8
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
            args[7] as T8,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
        flow8,
        flow9
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
            args[7] as T8,
            args[8] as T9,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
        flow8,
        flow9,
        flow10
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
            args[7] as T8,
            args[8] as T9,
            args[9] as T10,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
        flow8,
        flow9,
        flow10,
        flow11
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
            args[7] as T8,
            args[8] as T9,
            args[9] as T10,
            args[10] as T11,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
        flow8,
        flow9,
        flow10,
        flow11,
        flow12
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
            args[7] as T8,
            args[8] as T9,
            args[9] as T10,
            args[10] as T11,
            args[11] as T12,
        )
    }
}

private val HTML_TAG_REGEX = Regex("<[^>]*>")
private val HTML_ENTITIES = mapOf(
    "&amp;" to "&",
    "&lt;" to "<",
    "&gt;" to ">",
    "&quot;" to "\"",
    "&#39;" to "'",
    "&apos;" to "'",
    "&nbsp;" to " ",
)

// The randomizer API returns `notes` as a snippet of HTML (e.g. "<p>Casual w/ Boots Start</p>"),
// but this app only ever renders it as plain text, so strip tags and decode entities rather than
// showing the raw markup.
fun String.stripHtml(): String {
    var text = replace(Regex("(?i)<br\\s*/?>"), "\n")
        .replace(Regex("(?i)</p>"), "\n")
        .replace(HTML_TAG_REGEX, "")
    HTML_ENTITIES.forEach { (entity, replacement) -> text = text.replace(entity, replacement) }
    return text.trim()
}

fun getDateString(millis: Long): String {
    return runCatching {
        val instant = Instant.fromEpochMilliseconds(millis)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        val customFormat = LocalDateTime.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char('-')
            dayOfMonth()
            char('-')
            year()
        }

        localDateTime.format(customFormat)
    }.getOrDefault("") // Fallback if parsing fails
}