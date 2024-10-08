package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.cjanie.voltagedropcalculator.ui.theme.Typography

@Composable
fun Label(name: String, color: Color? = null) {
    val textStyle: TextStyle = Typography.labelLarge
    Text(
        text = name,
        fontFamily = textStyle.fontFamily,
        fontWeight = textStyle.fontWeight,
        fontSize = textStyle.fontSize,
        lineHeight = textStyle.lineHeight,
        letterSpacing = textStyle.letterSpacing,
        color = if(color != null) color else Color.Unspecified
    )
}