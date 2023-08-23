package com.futuretech.hyjl

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.lifecycle.lifecycleScope
import com.futuretech.base.base.BaseActivity
import com.futuretech.hyjl.recognition.RecognitionHelper
import com.futuretech.hyjl.ui.MainViewModel
import com.futuretech.hyjl.ui.RecState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val recognitionHelper: RecognitionHelper by lazy {
        RecognitionHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!recognitionHelper.prepareRecognition(viewModel)) {
            showToast("Recognition not available")
            return
        }

        setContent {
            RecognitionTest(vm = viewModel)
        }

        viewModel.recState.onEach {
            when (it) {
                is RecState.start -> recognitionHelper.startRecognition()
                is RecState.stop -> recognitionHelper.stopRecognition()
                RecState.idle -> {}
            }
        }.launchIn(lifecycleScope)

    }
}

//@Composable
//fun Test() {
//    val input = remember {
//        mutableStateOf("")
//    }
//    val canSend = remember {
//        derivedStateOf { input.value.isNotBlank() }
//    }
//}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecognitionTest(vm: MainViewModel) {
    val text: String by remember {
        vm.regText
    }
//    val readState = rememberPermissionState(permission = android.Manifest.permission.READ_PHONE_STATE)
//    LaunchedEffect(null) {
//        readState.launchPermissionRequest()
//    }
    // Camera permission state
    val permissionState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = text)
        Row {
            Button(onClick = {
                Log.d(TAG, "permissionState.status ${permissionState.status}")
                when (permissionState.status) {
                    is PermissionStatus.Denied -> {
                        permissionState.launchPermissionRequest()
                    }
                    is PermissionStatus.Granted -> {
                        vm.start()
                    }
                    else -> {
                        permissionState.launchPermissionRequest()
                    }
                }
            }) {
                Text(text = "Start")
            }

            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = {
                vm.stop()
            }) {
                Text(text = "Stop")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    RecognitionTest(vm = MainViewModel())
}


///**
// * currentRecomposeScope 的作用与View#invalidate方法类似，
// * 通过调用 currentRecomposeScope.invalidate()，它将使当前时刻的本地组合无效，
// * 并强制触发重组。一般用于手动触发重组。
// */
//data class User(val name: String)
//interface Presenter {
//    fun loadUser(after: @Composable () -> Unit): User
//}
//
//@Composable
//fun UserComposable(presenter: Presenter) {
//    val user = presenter.loadUser { currentRecomposeScope.invalidate() }
//    Text(text = "The load user: ${user.name}")
//}

//
//sealed class ViewState {
//    object Loading : ViewState()
//    object Error : ViewState()
//    data class Success(val user: User) : ViewState()
//}
//
//class DemoComposeVM : ViewModel() {
//    val user: Flow<ViewState> = flowOf(ViewState.Loading)
//}
//
//@Composable
//fun DemoViewModelFlow() {
//    val viewModel = viewModel<DemoComposeVM>()
//}