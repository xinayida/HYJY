package com.futuretech.base.demo.compose

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.futuretech.base.coroutine.exceptionContext
import com.futuretech.base.demo.realm.Item
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.futuretech.base.utils.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlin.random.Random

@Composable
fun MyApp() {
    val scope = rememberCoroutineScope()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            Button(onClick = {
                scope.launch {
                    snackBarHostState.showSnackbar("Something happened!")
                }
            }) {
                Text("Press me")
            }
        }
        SnackbarHost(hostState = snackBarHostState)
    }
}

@Composable
fun SearchScreen() {
    val scope = rememberCoroutineScope()
    var currentJob by remember {
        mutableStateOf<Job?>(null)
    }
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    Column {
        Row {
            TextField("Start typing to search",
                onValueChange = { text ->
                    currentJob?.cancel()
                    currentJob = scope.async {
                        delay(200)
//                        items = viewModel.search(query = text)
                    }
                })
        }
//        Row {
//            ItemsVerticalList(items)
//        }
    }
}

/**
 * rememberUpdatedState可以在不中断副作用的情况下感知外界的变化，
 * 一般用来获取观察状态的最新状态值。
 */
@Composable
fun MyScreen(onTimeOut: () -> Unit) {
    val currentOnTimeout by rememberUpdatedState(onTimeOut)
    // key为Unit时不会因为MyScreen的重组重新执行
    LaunchedEffect(Unit) {
        delay(300)
        currentOnTimeout() // 总是能获取到最新的 onTimeOut
    }
}

@Composable
fun UpdatedRememberExample() {
    var myInput by remember { mutableStateOf(0) }

    Column(Modifier.height(100.dp)) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            onClick = { myInput++ }
        ) {
            Text("Increase rememberInput: $myInput")
        }
        Calculation(input = myInput)
    }
}

@Composable
private fun Calculation(input: Int) {
//    val rememberUpdatedStateInput by rememberUpdatedState(input)
//    val rememberedInput by remember { mutableStateOf(input) }
//    Text("updatedInput: $rememberUpdatedStateInput, rememberedInput: $rememberedInput")

    Text(text = "input: $input")
}

@Composable
fun InputText(input: () -> String) {
    Text(input())
    Log.d("Stefan", "2222")
}

@Composable
fun TextFieldWrap(input: String, onValueChange: (String) -> Unit) {
    TextField(value = input, onValueChange = onValueChange, placeholder = {
        Text(text = "Type something here")
    })
    Log.d("Stefan", "333")
}

@Composable
fun Test() {
//    var input by remember {
//        mutableStateOf("")
//    }
    var input by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()
    val context = exceptionContext(name = "Test")

//    val canSend = remember {
//        derivedStateOf { input.value.isNotBlank() }
//    }
    Column(verticalArrangement = Arrangement.Center) {
//        TextField(value = input, onValueChange = { input = it }, placeholder = {
//            Text(text = "Type something here")
//        })
        Button(onClick = {
            scope.launch(context) {
                //TODO navigate to login screen
            }
        }) {
            Text(text = "Go to LoginScreen")
        }
        Log.d("Stefan", "1111")
        TextFieldWrap(input, onValueChange = { input = it })
        InputText {
            input
        }
//        Text(text = input)
    }
//    SendButton(canSend)
//    OtherCode(arg1, arg2)
//    OtherCode1(arg1, arg2)
}


/**
 * snapshotFlow 可以将 Compose 的 State 转换为 Flow。
 * 每当State变化时，flow就会发送新数据（但是冷流，调用collect才会发） snapshotFlow 会在收集到块时运行该块，
 * 并发出从块中读取的 State 对象的结果。当在 snapshotFlow 块中读取的 State 对象之一发生变化时，
 * 如果新值与之前发出的值不相等，Flow 会向其收集器发出新值（此行为类似于 Flow.distinctUntilChanged 的行为）。
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyScreen2() {
    val pagerState = rememberPagerState()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            // currentPage发生变化
        }
    }
    HorizontalPager(state = pagerState, pageCount = 10) { page ->

    }
}

class ImageRes(val imageIdRes: Int)

@Composable
fun loadNetWorkImage(
    url: String,
    imageRepository: ImageRepository
): State<Result> {
    // produceState 观察 url 和 imageRepository 两个参数，当它们发生变化时，producer会重新执行
    // produceState的实现是通过 remember { mutableStateOf() } + LaunchedEffect （具有学习意义）
    // produceState 中的任务会随着 LaunchedEffect 的 onDispose 被自动停止。
    return produceState<Result>(initialValue = Result.Loading, url, imageRepository) {
        // 通过挂起函数请求图片
        val image = imageRepository.load(url)
        // 根据请求结果设置 Result
        // 当 Result 变化时，读取此 State 的 Composable 会触发重组
        value = if (image == null) {
            Result.Error(IllegalAccessException("image is null"))
        } else {
            Result.Success(image)
        }
    }
}

class ImageRepository {
    /**
     * Returns a drawable resource or null to simulate Result with Success or Error states
     */
    suspend fun load(url: String): ImageRes? {
        delay(2000)
        // Random is added to return null if get a random number that is zero.
        // Possibility of getting null is 1/4
        return null
    }
}


@Composable
fun TodoList(highPriorityKeywords: List<String> = listOf("Review", "Unblock", "Compose")) {
    val todoTasks = remember { mutableStateListOf<String>() }

    // Calculate high priority tasks only when the todoTasks or highPriorityKeywords
    // change, not on every recomposition
    val highPriorityTasks by remember(highPriorityKeywords) {
        derivedStateOf { todoTasks.filter { str -> highPriorityKeywords.any { str.contains(it) } } }
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            items(highPriorityTasks) { /* ... */
                Text(text = it)
            }
            items(todoTasks) {
                Text(text = it)
            }
        }
        /* Rest of the UI where users can add elements to the list */
    }
}


@Composable
fun SearchScreen2() {
    val postList = remember { mutableStateListOf<String>() }
    val keyWord by remember { mutableStateOf("") }

    // 只有当输出的 DerivedState变化才会导致Composable重组
    val result by remember {
        derivedStateOf { postList.filter { it.contains(keyWord) } }
    }

    // 这里 postList 和 keyWord任意一个变化时，会更新 result
    val result2 = remember(postList, keyWord) {
        postList.filter { it.contains(keyWord) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(result) { item ->
                Text(item)
            }
        }
    }
}


@Composable
fun DerivedStateOfExample() {
    var numberOfItems by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .height(100.dp)
    ) {
        Surface {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Amount to buy: $numberOfItems", modifier = Modifier.weight(1f))
                IconButton(onClick = { numberOfItems++ }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { if (numberOfItems > 0) numberOfItems-- }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "remove")
                }
            }
        }

        Surface {
            val derivedStateMax by remember {
                derivedStateOf { numberOfItems > 5 }
            }

            if (derivedStateMax) {
                println("🤔 COMPOSING...")
                Text(
                    "You cannot buy more than 5 items",
                    color = Color(0xffE53935),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(getRandomColor())
                )
            }
        }
        Surface {
            val derivedStateMax2 by remember(numberOfItems) {
                mutableStateOf(numberOfItems > 5)
            }

            if (derivedStateMax2) {
                println("🤔 COMPOSING...2")
                Text(
                    "You cannot buy more than 5 items",
                    color = Color(0xffE53935),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(getRandomColor())
                )
            }
        }
    }
}

private fun getRandomColor(): Color {
    // 生成 0-255 之间的随机数
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)

    // 构建 Color 对象
    return Color(red, green, blue)
}