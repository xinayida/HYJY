package com.futuretech.base.coroutine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

@Composable
fun exceptionContext(name: String): CoroutineContext = remember {
    SupervisorJob() + Dispatchers.Main + CoroutineExceptionHandler { context, throwable ->
        println("${context[CoroutineName]} 发生了异常: $throwable")
    } + CoroutineName(name)
}