package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.onCopperColor

@Composable
fun SubmitButton(text: String, onClick: () -> Unit, enabled: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonColors(
                containerColor = copperColor,
                contentColor = onCopperColor,
                disabledContainerColor = TextFieldDefaults.colors().disabledContainerColor,
                disabledContentColor = TextFieldDefaults.colors().disabledTextColor
            )
        ) {
            Text(text = text)
        }
    }
}