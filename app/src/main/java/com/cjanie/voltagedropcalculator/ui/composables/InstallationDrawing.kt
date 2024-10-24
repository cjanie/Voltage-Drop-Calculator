package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.cjanie.voltagedropcalculator.ui.InstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.theme.copperColor

@Composable
fun InstallationDrawing(installationPresenter: InstallationSetUpViewModel.InstallationPresenter) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Label(name = installationPresenter.functionalContext)
            Label(name = installationPresenter.electricitySupply)
            Label(name = installationPresenter.tension)
        }

        val textMeasurer = rememberTextMeasurer()
        val inputCablePresenter = installationPresenter.inputCablePresenter
        val inputCableText = inputCablePresenter.cableText
        val outputCircuitsPresenter = installationPresenter.outputCircuitsPresenter
        val outputCircuitsText = outputCircuitsPresenter?.cableText
        val style = TextStyle(
            fontSize = 16.sp,
            color = Color.Black
        )
        val inputCableTextLayoutResult = remember(inputCableText, style) {
            textMeasurer.measure(inputCableText, style)
        }
        val outputCircuitsTextLayoutResult = remember(outputCircuitsText, style) {
            if(outputCircuitsText != null) {
                textMeasurer.measure(outputCircuitsText, style)
            } else null
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = 1000f
            val canvasHeight = 1000f
            drawLine(
                start = Offset(x = canvasWidth/2, y = 0f),
                end = Offset(x = canvasWidth/2, y = canvasHeight/2),
                color = copperColor,
                strokeWidth = 4f
            )

            drawText(inputCableTextLayoutResult)

            if (outputCircuitsPresenter != null) {
                drawLine(
                    start = Offset(x = canvasWidth/3, y = canvasHeight/2),
                    end = Offset(x = canvasWidth, y = canvasHeight/2),
                    color = copperColor,
                    strokeWidth = 4f
                )
                drawLine(
                    start = Offset(x = canvasWidth/3, y = canvasHeight/2),
                    end = Offset(x = canvasWidth/3, y = canvasHeight - 100),
                    color = copperColor,
                    strokeWidth = 4f
                )
                drawLine(
                    start = Offset(x = canvasWidth*2/3, y = canvasHeight/2),
                    end = Offset(x = canvasWidth*2/3, y = canvasHeight - 100),
                    color = copperColor,
                    strokeWidth = 4f
                )
                drawLine(
                    start = Offset(x = canvasWidth, y = canvasHeight/2),
                    end = Offset(x = canvasWidth, y = canvasHeight - 100),
                    color = copperColor,
                    strokeWidth = 4f
                )
                drawCircle(
                    color = Color.Red,
                    radius = 50f,
                    center = Offset(x = canvasWidth/3, y = canvasHeight - 50)
                )
                drawCircle(
                    color = Color.Green,
                    radius = 50f,
                    center = Offset(x = canvasWidth*2/3, y = canvasHeight - 50)
                )
                drawCircle(
                    color = Color.Blue,
                    radius = 50f,
                    center = Offset(x = canvasWidth, y = canvasHeight - 50)
                )
                if (outputCircuitsTextLayoutResult != null) {
                    drawText(outputCircuitsTextLayoutResult, topLeft = Offset(x = 0f, y= canvasHeight/2))
                }
            }


        }

    }
}