package com.cjanie.voltagedropcalculator.ui.composables.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.onCopperColor

@Composable
fun Header(modifier: Modifier, text: String, color: Color = copperColor, textColor: Color = onCopperColor) {
    Row(
        modifier = modifier.fillMaxWidth()
            .background(color = color)
    ) {
        Title(text = text, textColor = textColor)
    }
}