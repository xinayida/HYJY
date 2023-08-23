package com.futuretech.base.extend

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color

fun Modifier.scrim(color: Color): Modifier = drawWithContent {
    drawContent()
    drawRect(color = color)
}

fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier = composed {
    clickable(
        onClick = onClick,
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    )
}