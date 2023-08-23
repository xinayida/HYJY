package com.futuretech.base.demo.navigation

import android.os.Parcelable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(val userId : Int, val name : String): Parcelable


const val KEY = "my_text"

@Composable
fun NavigateBackWithResultExample() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "screen01") {
        composable("screen01") { entry ->
            val text = entry.savedStateHandle.get<String>(KEY) // 尝试从NavBackStackEntry中获取值
            Screen01(text) {
                navController.navigate("screen02")
            }
        }
        composable("screen02") {
            Screen02 { result ->
                navController.previousBackStackEntry?.savedStateHandle?.set(KEY, result) // 向前一个Entry回传结果
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun Screen01(text: String?, onNavigateBtnClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(text = "当前页面 Screen01", fontSize = 16.sp)
        text?.let { Text(text = "来自screen02的结果：$text", fontSize = 16.sp) }
        Button(onClick = onNavigateBtnClick) {
            Text(text = "Go to screen02")
        }
    }
}

@Composable
fun Screen02(onNavigateBtnClick: (String) -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        var text by remember { mutableStateOf("") }
        Text(text = "当前页面 Screen02", fontSize = 16.sp)
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.width(300.dp)
        )
        Button(onClick = { onNavigateBtnClick(text) }) {
            Text(text = "Go Back")
        }
    }
}