package com.envyglit.core.ui.utils

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
        return result.uppercase(Locale.ROOT)
    }
}
