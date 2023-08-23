package com.futuretech.hyjl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MyComposable() {
    // 定义状态用于保存显示的文本
    var buttonText by remember { mutableStateOf("Click Me") }

    // 获取记住的协程作用域
    val coroutineScope = rememberCoroutineScope()

    Column {
        // 显示文本
        Text(
            text = buttonText,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        // 按钮点击事件
        Button(
            onClick = {
                // 在协程作用域中执行异步操作
                coroutineScope.launch {
                    // 模拟一个异步操作（延迟 1 秒）
                    delay(1000)

                    // 更新按钮文本
                    buttonText = "Clicked!"
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Click Me")
        }
    }
}

@Preview
@Composable
fun PreviewMyComposable() {
    MaterialTheme {
        Surface {
            MyComposable()
        }
    }
}






