package com.cjanie.voltagedropcalculator.ui.composables.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.theme.Typography

@Composable
fun Title(
    text: String,
    textColor: Color,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
) {

    val textStyle = Typography.titleLarge

    Text(
        text = text,
        fontFamily = textStyle.fontFamily,
        fontWeight = textStyle.fontWeight,
        fontSize = textStyle.fontSize,
        lineHeight = textStyle.lineHeight,
        letterSpacing = textStyle.letterSpacing,
        color = textColor,
        modifier = modifier,
        textAlign = textAlign
    )

}