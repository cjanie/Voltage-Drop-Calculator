package com.cjanie.voltagedropcalculator.ui.composables.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.theme.paddingMedium

@Composable
fun LabeledText(label: String, text: String, textColor: Color = Color.Unspecified, modifier: Modifier = Modifier
    .fillMaxWidth()
    .padding(paddingMedium)) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Label(text = label, color = textColor, modifier = Modifier)
        Text(text = text, color = textColor)
    }
}