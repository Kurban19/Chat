package com.shkiper.chat.util

import java.util.*

object Utils {

    fun toInitials(firstName: String?, lastName: String? = ""): String {
        val f = firstName?.trim() ?: ""
        val l = lastName?.trim() ?: ""

        val result =  if(f.isEmpty() && l.isEmpty()){
            return ""
        }
        else if (f.isEmpty() && l.isNotEmpty()){
            l[0].toString()
        }
        else if(f.isNotEmpty() && l.isEmpty()){
            f[0].toString()
        }
        else {
            f[0].toString() + l[0].toString()
        }
        return result.toUpperCase(Locale.ROOT)
    }

    fun transliteration(s: String, divider: String = " "): String {
        val f: Array<String> = arrayOf("А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ч", "Ц", "Ш", "Щ", "Э", "Ю", "Я", "Ы", "Ъ", "Ь", "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь")
        val t: Array<String> = arrayOf("A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "I", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ch", "C", "Sh", "'Sh'", "E", "Yu", "Ya", "Y", "", "", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "sh", "e", "yu", "ya", "y", "", "")

        var res: String = ""

        for (i in s.indices) {
            var add: String = s.substring(i, i + 1)
            for (j in f.indices) {
                if (f[j] == add) {
                    add = t[j]
                    break
                }
            }
            res += add
        }
        return res.replace(" ", divider)
    }
}
