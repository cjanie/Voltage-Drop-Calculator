package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cjanie.voltagedropcalculator.ui.theme.Typography

@Composable
fun Title(name: String, textColor: Color) {
    val textStyle = Typography.titleLarge
    Text(
        text = name,
        fontFamily = textStyle.fontFamily,
        fontWeight = textStyle.fontWeight,
        fontSize = textStyle.fontSize,
        lineHeight = textStyle.lineHeight,
        letterSpacing = textStyle.letterSpacing,
        color = textColor
    )
}