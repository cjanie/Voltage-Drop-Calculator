package com.cjanie.voltagedropcalculator.ui.composables.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NumberInput(name: String, select: (value: Float) -> Unit, enabled: Boolean) {
    var value by remember {
        mutableStateOf("")
    }
    TextField(
        label = { Label(text = name) },
        value = value,
        onValueChange = {
            value = it
            if (value.isNotEmpty()) select(value.toFloat())
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            println("done")
        }),
        colors = OutlinedTextFieldDefaults.colors(),
        enabled = enabled
    )
}