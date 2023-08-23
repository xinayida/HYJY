package com.futuretech.base.widgets

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futuretech.base.extend.clickableNoRipple
import com.futuretech.base.theme.SystemBarTheme

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun MessageDialog(
    visible: Boolean,
    title: String,
    leftButtonText: String,
    rightButtonText: String,
    onClickLeftButton: () -> Unit,
    onClickRightButton: () -> Unit
) {
    val onClickLeft by rememberUpdatedState(onClickLeftButton)
    val onClickRight by rememberUpdatedState(onClickRightButton)
    BackHandler(enabled = visible) {

    }
    SystemBarTheme(
        navigationBarColor = if (visible) {
            Color.Transparent
        } else {
            MaterialTheme.colorScheme.background
        }
    )
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        Box(
            modifier = Modifier
                .clickableNoRipple {

                }
                .fillMaxSize()
                .background(color = Color(0x99000000))
                .padding(horizontal = 24.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(alignment = Alignment.Center)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(size = 12.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    text = title,
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 0.4.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 56.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .fillMaxHeight()
                            .clickable(onClick = onClickLeft)
                            .wrapContentSize(align = Alignment.Center),
                        text = leftButtonText,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(width = 0.4.dp)
                            .background(color = MaterialTheme.colorScheme.outlineVariant)
                    )
                    Text(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .fillMaxHeight()
                            .clickable(onClick = onClickRight)
                            .wrapContentSize(align = Alignment.Center),
                        text = rightButtonText,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMessageDialog() {
    MessageDialog(
        visible = true,
        title = "title",
        leftButtonText = "left",
        rightButtonText = "right",
        onClickLeftButton = {},
        onClickRightButton = {}
    )
}