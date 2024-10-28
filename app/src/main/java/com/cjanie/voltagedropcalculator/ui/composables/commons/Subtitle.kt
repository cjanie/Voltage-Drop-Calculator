package com.cjanie.voltagedropcalculator.ui.composables.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.theme.Typography
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor

@Composable
fun Subtitle(
    text: String,
    color: Color = greenWarningColor,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier.fillMaxWidth().padding(20.dp)) {
    Label(
        text = text,
        color = color,
        textAlign = textAlign,
        textStyle = Typography.titleMedium,
        modifier = modifier
    )
}