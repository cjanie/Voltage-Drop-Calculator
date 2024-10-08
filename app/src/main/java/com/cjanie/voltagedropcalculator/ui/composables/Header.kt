package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.onCopperColor

@Composable
fun Header(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = copperColor)
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Title(name = text, textColor = onCopperColor)
    }
}