package ru.skillbranch.devintensive.utils

import java.lang.StringBuilder
import java.util.*

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.trim()?.split(" ", limit = 2)
        var firstName = parts?.getOrNull(0)?.trim()
        var lastName = parts?.getOrNull(1)?.trim()
        if (firstName.isNullOrBlank()) firstName = null
        if (lastName.isNullOrBlank()) lastName = null
        return firstName to lastName
    }

    @ExperimentalStdlibApi
    fun transliteration(payload: String, divider: String = " "): String {
        val stringBuilder = StringBuilder()
        payload.forEach { letter ->
            if (letter.isWhitespace()) stringBuilder.append(divider)
            else if (transliterations.containsKey(
                    letter.toString().toLowerCase(Locale.ROOT)
                ) && letter.isUpperCase()
            ) {
                stringBuilder.append(
                    transliterations[letter.toString()
                        .toLowerCase(Locale.ROOT)]?.capitalize(Locale.ROOT)
                )
            } else if (transliterations.containsKey(
                    letter.toString().toLowerCase(Locale.ROOT)
                ) && letter.isLowerCase()
            ) {
                stringBuilder.append(
                    transliterations[letter.toString()]
                )
            } else stringBuilder.append(letter)
        }
        return stringBuilder.toString()
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val stringBuilder = StringBuilder()
        if (!firstName.isNullOrBlank()) {
            stringBuilder.append(firstName.trim().first().toUpperCase())
        }
        if (!lastName.isNullOrBlank()) {
            stringBuilder.append(lastName.trim().first().toUpperCase())
        }
        return when {
            stringBuilder.isBlank() -> null
            else -> stringBuilder.toString()
        }
    }

    private val transliterations: Map<String, String> = mapOf(
        Pair("а", "a"),

        Pair("б", "b"),

        Pair("в", "v"),

        Pair("г", "g"),

        Pair("д", "d"),

        Pair("е", "e"),

        Pair("ё", "e"),

        Pair("ж", "zh"),

        Pair("з", "z"),

        Pair("и", "i"),

        Pair("й", "i"),

        Pair("к", "k"),

        Pair("л", "l"),

        Pair("м", "m"),

        Pair("н", "n"),

        Pair("о", "o"),

        Pair("п", "p"),

        Pair("р", "r"),

        Pair("с", "s"),

        Pair("т", "t"),

        Pair("у", "u"),

        Pair("ф", "f"),

        Pair("х", "h"),

        Pair("ц", "c"),

        Pair("ч", "ch"),

        Pair("ш", "sh"),

        Pair("щ", "sh'"),

        Pair("ъ", ""),

        Pair("ы", "i"),

        Pair("ь", ""),

        Pair("э", "e"),

        Pair("ю", "yu"),

        Pair("я", "ya")
    )
}