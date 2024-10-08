package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Dropdown(name: String, options: Array<String>, select: (itemPosition: Int) -> Unit) {
    var isDropDownExpanded by remember {
        mutableStateOf(false)
    }

    var itemPosition: Int? by remember {
        mutableStateOf(null)
    }
    if (itemPosition != null) select(itemPosition!!)

    Column(modifier = Modifier.padding(8.dp)) {
        TextField(
            label = { Label(name = name) },
            value = if (itemPosition == null) "" else options[itemPosition!!],
            onValueChange = { },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { isDropDownExpanded = true }),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = if (isDropDownExpanded) OutlinedTextFieldDefaults.colors().focusedTextColor else MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = if (isDropDownExpanded) OutlinedTextFieldDefaults.colors().focusedLabelColor else MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = if (isDropDownExpanded) OutlinedTextFieldDefaults.colors().focusedPlaceholderColor else MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = if (isDropDownExpanded) OutlinedTextFieldDefaults.colors().focusedLabelColor else MaterialTheme.colorScheme.onSurfaceVariant,
                //For Icons
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        DropdownMenu(
            expanded = isDropDownExpanded,
            onDismissRequest = { isDropDownExpanded = false },
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        isDropDownExpanded = false
                        itemPosition = index
                    }
                )
            }
        }
    }

}