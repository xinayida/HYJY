package com.futuretech.hyjl.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futuretech.hyjl.recognition.ASRResultListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class RecState {
    object idle : RecState()
    object start : RecState()
    object stop : RecState()
}

class MainViewModel : ViewModel(), ASRResultListener {
    var regText = mutableStateOf("Demo")
    var recState = MutableStateFlow<RecState>(RecState.idle)
        private set

    override fun onPartialResult(result: String) {
        regText.value = result
    }

    override fun onFinalResult(result: String) {
        viewModelScope.launch {
            delay(300)
            regText.value = result
        }
    }

    fun start() {
        recState.value = RecState.start
    }

    fun stop(){
        recState.value = RecState.stop
    }


}