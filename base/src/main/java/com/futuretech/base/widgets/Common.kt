package com.futuretech.base.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futuretech.base.R

@Composable
fun CommonOutlinedTextField(
    modifier: Modifier,
    value: String,
    label: String,
    singleLine: Boolean = false,
    maxLines: Int = 4,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        label = {
            Text(
                text = label, fontSize = 14.sp
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun CommonButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 8.dp
            ),
        content = {
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.White
            )
        },
        onClick = onClick
    )
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            content { expanded = !expanded }
        }
    }
}

/**
 * Dropdown menu demo
 */
@Composable
private fun MoreTasksMenu(
    onClearCompletedTasks: () -> Unit,
    onRefresh: () -> Unit
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(Icons.Filled.MoreVert, "more")
        }
    ) { closeMenu ->
        DropdownMenuItem(text = { Text(text = "clear") }, onClick = { onClearCompletedTasks(); closeMenu() })
        DropdownMenuItem(text = { Text(text = "refresh") }, onClick = { onRefresh(); closeMenu() })
    }
}
