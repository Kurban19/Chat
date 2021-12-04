package com.envyglit.core.ui.extensions

import android.content.Context

fun Context.dpToPx(dp:Int): Float{ //TODO delete when moved to compose
    return dp.toFloat() * this.resources.displayMetrics.density
}