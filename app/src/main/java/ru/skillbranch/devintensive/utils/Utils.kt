package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName:String?) : Pair<String?, String?> {
        var pos: String? = fullName

        if(pos == null){
            return null to null
        }
        pos = pos.trim()
        while (pos!!.contains("  ")) {
            val replace: String = pos.replace("  ", " ")
            pos = replace
        }
        if(pos == "" || pos == " "){
            return null to null
        }
        pos.trim()
        var parts : List<String>? = pos.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        if(lastName == " " || lastName == ""){
            return firstName to null
        }
        if(firstName == " " || firstName == ""){
            return null to lastName
        }
        return firstName to lastName
    }



    fun transliteration(s: String, divider: String = " "): String{
        val f: Array<String> = arrayOf("А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ч", "Ц", "Ш", "Щ", "Э", "Ю", "Я", "Ы", "Ъ", "Ь", "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь")
        val t: Array<String> = arrayOf("A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "I", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ch", "C", "Sh", "'Sh'", "E", "Yu", "Ya", "Y", "", "", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "sh", "e", "yu", "ya", "y", "", "")

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

        if (str != null) {
            str = str.trim()
        }

        if (str2 != null) {
            str2 = str2.trim()
        }


        if(firstName == null && lastName == null){
            return null
        }
        else if(firstName == " " && lastName == ""){
            return null
        }
        else if(firstName == " " && lastName == null){
            return null
        }
        else if(firstName == null && lastName == ""){
            return null
        }
        else if(firstName == null && lastName!!.isNotEmpty()){
            res += str2!![0]
        }
        else if(firstName!!.isNotEmpty() && lastName == null){
            res += str!![0]
        }
        else if (firstName!!.isNotEmpty() && lastName!!.isNotEmpty()) {

            res += str!![0]
            res += str2!![0]
        }

        return res.toUpperCase()
    }

}