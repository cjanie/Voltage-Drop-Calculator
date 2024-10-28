package com.cjanie.voltagedropcalculator.ui.composables.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.cjanie.voltagedropcalculator.ui.theme.Typography

@Composable
fun Label(
    text: String,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    textStyle: TextStyle = Typography.labelLarge,
    modifier: Modifier = Modifier.fillMaxWidth()
    ) {

    Text(
        text = text,
        fontFamily = textStyle.fontFamily,
        fontWeight = textStyle.fontWeight,
        fontSize = textStyle.fontSize,
        lineHeight = textStyle.lineHeight,
        letterSpacing = textStyle.letterSpacing,
        color = color,
        textAlign = textAlign,
        modifier = modifier
    )
    
}