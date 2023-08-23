package com.futuretech.base.extend

import androidx.navigation.NavController

fun NavController.setResult(key: String, result: Any){
    previousBackStackEntry?.savedStateHandle?.set(key, result)
}