package com.futuretech.base.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.futuretech.base.provider.AppTheme
import com.futuretech.base.provider.AppThemeProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SystemBarTheme(
    appTheme: AppTheme = AppThemeProvider.appTheme,
    statusBarColor: Color = Color.Transparent,
    statusBarDarkIcons: Boolean = appTheme == AppTheme.Light || appTheme == AppTheme.Gray,
    navigationBarColor: Color = MaterialTheme.colorScheme.background,
    navigationDarkIcons: Boolean = appTheme != AppTheme.Dark
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = statusBarColor,
        darkIcons = statusBarDarkIcons
    )
    systemUiController.setNavigationBarColor(
        color = navigationBarColor,
        darkIcons = navigationDarkIcons,
        navigationBarContrastEnforced = false
    )
}