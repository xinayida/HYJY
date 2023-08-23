package com.futuretech.base.widgets

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.futuretech.base.extend.clickableNoRipple

@Composable
fun LoadingDialog(visible: Boolean) {
    if (visible) {
        BackHandler(enabled = true) {

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickableNoRipple {

                }
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(alignment = Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}