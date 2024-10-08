package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LabeledText(label: String, text: String, textColor: Color? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Label(name = label, color = textColor)
        Text(text = text, color = if(textColor != null) textColor else Color.Unspecified)
    }
}