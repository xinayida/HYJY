package com.futuretech.base.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.futuretech.base.provider.ToastProvider
import com.futuretech.base.theme.ApplicationTheme
import com.futuretech.base.theme.SystemBarTheme

open class BaseActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewModel> viewModelsInstance(crossinline create: () -> T): Lazy<T> {
        return viewModels(factoryProducer = {
            object : ViewModelProvider.NewInstanceFactory() {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return create() as T
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    protected fun setContent(
        systemBarTheme: @Composable () -> Unit = {
            SystemBarTheme()
        },
        content: @Composable () -> Unit
    ) {
        setContent(
            parent = null,
            content = {
                ApplicationTheme {
                    systemBarTheme()
                    content()
                }
            }
        )
    }

    protected fun showToast(msg: String) {
        ToastProvider.showToast(msg = msg)
    }

    protected inline fun <reified T : Activity> startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

}