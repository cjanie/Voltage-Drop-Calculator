package com.cjanie.voltagedropcalculator.ui.composables.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.onCopperColor

@Composable
fun Header(
    text: String,
    color: Color = copperColor,
    textColor: Color = onCopperColor,
    modifier: Modifier = Modifier
        .fillMaxWidth()
) {
    Row(
        modifier = modifier.background(color = color),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Title(text = text, textColor = textColor)
    }
}