package ru.skillbranch.devintensive.extensions


fun String.truncate(i: Int = 16): String{
    if(this.length < 16){
        return this
    }

    val res = this.substring(0, i)

    return "$res..."
}


fun String.replaceGarbage(): String {
    return this.replace("[", "").replace("]", "")
}


fun String.stripHtml(): String{
    var pos = this.split(">")
    var result = pos[1]
    while(result.contains("  ")){
        val replace = result.replace("  ", " ")
        result = replace
    }


    return result.replace("</p", "")
}