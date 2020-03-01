package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName:String?) : Pair<String?, String?> {
        if(fullName == null){
            return null to null
        }
        var parts : List<String>? = fullName!!.split(" ")


        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)


        return firstName to lastName
    }


    fun transliteration(s: String, divider: String = " "): String{
        val f: Array<String> = arrayOf("А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ч", "Ц", "Ш", "Щ", "Э", "Ю", "Я", "Ы", "Ъ", "Ь", "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь")
        val t: Array<String> = arrayOf("A", "B", "V", "G", "D", "E", "Jo", "Zh", "Z", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ch", "C", "Sh", "Csh", "E", "Ju", "Ja", "Y", "`", "'", "a", "b", "v", "g", "d", "e", "jo", "zh", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "y", "`", "'")

        var res: String = ""

        for (i in 0 until s.length){
            var add: String = s.substring(i, i + 1)
            for(j in 0 until f.size){
                if(f[j] == add) {
                    add = t[j]
                    break
                }
            }
            res += add
        }
        return res.replace(" ", divider)
    }

    fun toInitials(firstName: String?, lastName: String? = ""): String?{
        var str: String? = firstName?.let { transliteration(it) }
        var str2: String? = lastName?.let { transliteration(it) }


        var res: String =""


        if(firstName == null && lastName == null){
            return "null"
        }
        else if(firstName == " "){
            return "null"
        }
        else if(lastName == null){
            res += str!![0]
        }
        else if(str != "" && str2 == ""){
            res += str!![0]
        }
        else if(str == "" && str2 != ""){
            res += str2!![0]
        }
        else {
            res += str!![0]
            res += str2!![0]
        }
        return res.toUpperCase()
    }
}