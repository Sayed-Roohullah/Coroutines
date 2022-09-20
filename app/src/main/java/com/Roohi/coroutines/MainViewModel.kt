package com.Roohi.coroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    val tag:String = "SaqiJani"
    init {
        viewModelScope.launch {
            while (true){
                delay(500)
                Log.d(tag,"hello from saqi")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(tag,"View Model Destroyed")
    }
}