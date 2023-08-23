package com.futuretech.base.demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

class ViewModelDemo : ViewModel() {
    fun test() {
        val job = viewModelScope.launch {
            println("viewModelScope ${currentCoroutineContext()}")
//            withContext(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                println("withContext before ${currentCoroutineContext()}")
                delay(500)
                println("withContext after ${currentCoroutineContext()}")
            }

            withContext(Dispatchers.IO){
                println("withContext2 ${currentCoroutineContext()}")
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(200)
            job.cancel()
            println("viewModelScope ${currentCoroutineContext()}")
            withContext(Dispatchers.IO) {
                println("withContext2 ${currentCoroutineContext()}")
            }
        }


    }
}