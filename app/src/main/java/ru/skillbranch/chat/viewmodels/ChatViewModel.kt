package ru.skillbranch.chat.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.chat.repositories.PreferencesRepository

class ChatViewModel : ViewModel() {

    private val repository: PreferencesRepository = PreferencesRepository
    private val appTheme = MutableLiveData<Int>()





    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES){
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        }
        else{
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }



}