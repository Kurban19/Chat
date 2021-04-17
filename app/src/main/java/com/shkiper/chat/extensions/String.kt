package com.shkiper.chat.extensions


fun String.truncate(i: Int = 16): String{
    if(this.length < 16){
        return this
    }

    val res = this.substring(0, i)

    return "$res..."
}
