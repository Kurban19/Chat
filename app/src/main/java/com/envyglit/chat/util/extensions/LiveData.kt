package com.envyglit.chat.util.extensions

import androidx.lifecycle.MutableLiveData

fun <T> mutableLiveData(defaultValue: T? = null): MutableLiveData<T> {
    val data = MutableLiveData<T>()

    defaultValue?.let {
        data.value = defaultValue
    }

    return data
}