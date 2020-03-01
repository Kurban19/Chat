package ru.skillbranch.devintensive.extensions


import java.text.SimpleDateFormat

import java.util.*
import java.util.concurrent.TimeUnit



const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern:String="HH:mm:ss dd:MM:yy"):String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND):Date{
    var time = this.time
    time += when(units){
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE-> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY-> value * DAY
    }
    this.time = time
    return this
}


fun Date.humanizeDiff(date: Date = Date()): String{
    var res: String = ""
    var sdf: SimpleDateFormat = SimpleDateFormat("HH:mm:ss dd:MM:yy", Locale("ru"))
    var past:Date? = sdf.parse(this.format())
    var current:Date? = sdf.parse(date.format())

    println(this.format())

    //val diffInMillies = Math.abs(current!!.getTime() - past!!.getTime())
    val diffInMillies = current!!.getTime() - past!!.getTime()
    val days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
    val hours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS)
    val minutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS)
    val diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS)

    res +=
        when(true){
            diff > 31536000 -> "более года назад"
            diff > 86400 -> "${TimeUnits.DAY.plural(days)} назад"
            diff >= 3600 -> "${TimeUnits.HOUR.plural(hours)} назад"
            diff >= 60 -> "${TimeUnits.MINUTE.plural(minutes)} назад"
            diff > 0 -> "${TimeUnits.SECOND.plural(diff)} назад"
            diff > -60 -> "через ${TimeUnits.SECOND.plural(diff)}"
            diff > -3600 -> "через ${TimeUnits.MINUTE.plural(minutes)}"
            diff > -86400 -> "через ${TimeUnits.HOUR.plural(hours)}"
            diff > -31536000 -> "через ${TimeUnits.DAY.plural(days)}"
            diff < -31536000 -> "более чем через год"
            else -> "что то пошло не так"
        }

    return res
}

enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(input: Long): String {
        var times: Array<String> =
            when(true) {
                this == SECOND -> arrayOf("секунду", "секунды", "секунд")
                this == MINUTE -> arrayOf("минуту", "минуты", "минут")
                this == HOUR -> arrayOf("час", "часа", "часов")
                this == DAY -> arrayOf("день", "дня", "дней")
                else -> arrayOf("null", "null", "null")
            }
        var number = input

        var str = java.lang.Long.toString(number) + " "
        number = Math.abs(number)



        if (number % 10 == 1L && number % 100 != 11L) {
            str += times[0]
        } else if (number % 10 >= 2 && number % 10 <= 4 && (number % 100 < 10 || number % 100 >= 20)) {
            str += times[1]
        } else {
            str += times[2]
        }

        return str.replace("-", "")
    }

}