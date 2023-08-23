package com.futuretech.hyjl

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.futuretech.base.base.BaseActivity

class SecondActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            demoContent()
        }
    }
}

data class Note(val id: Int, val title: String, val content: String)

@Composable
fun NotesList(notes: List<Note>) {
    LazyColumn {
        items(items = notes,
            key = { note ->
                note.id
            }) { note ->
            NoteRow(note)
        }
    }
}

@Composable
fun NoteRow(note: Note) {
    Text(text = note.title)
}

@Composable
fun demoContent() {
    var name by remember {
        mutableStateOf("123")
    }
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
    }
}


@Composable
fun BackPressHandler(enabled: Boolean = true, onBackPressed: () -> Unit) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val currentOnBack by rememberUpdatedState(onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    LaunchedEffect(Unit) {

    }

    // backDispatcher 发生变化时重新执行
    DisposableEffect(backDispatcher) {
        backDispatcher?.addCallback(backCallback) // onActive时添加回调
        // 当 Composable 进入 onDispose 时执行
        onDispose {
            backCallback.remove() // onDispose时移除回调 避免内存泄漏
        }
    }
}
