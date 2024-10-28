package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cjanie.voltagedropcalculator.ui.composables.commons.Label
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationViewModel
import com.cjanie.voltagedropcalculator.ui.theme.copperColor

@Composable
fun InstallationDrawing(installationPresenter: InstallationViewModel.InstallationPresenter) {

    Column()
         {

        InstallationSpecifications(installationPresenter)

        InstallationCanvas(
            inputCablePresenter = installationPresenter.inputCablePresenter,
            outputCircuitsPresenter = installationPresenter.outputCircuitsPresenter
        )


    }
}

@Composable
fun InstallationSpecifications(specifications: InstallationViewModel.InstallationSpecifications) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val modifier = Modifier.padding(20.dp)
        Label(text = specifications.functionalContext, modifier = modifier)
        Label(text = specifications.electricitySupply, modifier = modifier)
        Label(text = specifications.tension, modifier = modifier)
    }
}

@Composable
fun InstallationCanvas(
    inputCablePresenter: InstallationViewModel.CablePresenter,
    outputCircuitsPresenter: InstallationViewModel.CablePresenter?
) {
    var columnHeightInPx by remember {
        mutableFloatStateOf(0f)
    }
    var columnWidthInPx by remember {
        mutableFloatStateOf(0f)
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .onGloballyPositioned { coordinates ->
                // Set column height using the LayoutCoordinates
                columnHeightInPx = coordinates.size.height.toFloat()
                columnWidthInPx = coordinates.size.width.toFloat()
            }
    ) {

        val textMeasurer = rememberTextMeasurer()
        val inputCableText = inputCablePresenter.cableText
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


        val horizontalPadding = 20.dp
        val paddingBottom = 20.dp
        Canvas(modifier = Modifier.padding(horizontalPadding, 0.dp, horizontalPadding, paddingBottom)) {
            val canvasWidthInPx = columnWidthInPx - horizontalPadding.toPx() * 2
            val canvasHeightInPx = columnHeightInPx - paddingBottom.toPx()

            // Draw input cable
            val inputCableStart = Offset(x = canvasWidthInPx / 2, y = 0f)
            val inputCableEnd = Offset(x = canvasWidthInPx / 2, y = canvasHeightInPx / 2)
            drawLine(
                start = inputCableStart,
                end = inputCableEnd,
                color = copperColor,
                strokeWidth = 4f
            )

            // Input cable legend
            drawText(inputCableTextLayoutResult, topLeft = Offset(x = 0f, y = inputCableEnd.y / 3))

            // Terminal
            val circleRadius = 50f

            if (outputCircuitsPresenter == null) {
                drawCircle(
                    color = Color.Red,
                    radius = circleRadius,
                    center = Offset(x = inputCableEnd.x, y = inputCableEnd.y + circleRadius)
                )
            } else {
                // Output circuits
                // Horizontal line
                val dividerStart = Offset(x = canvasWidthInPx/3, y = inputCableEnd.y)
                val dividerEnd = Offset(x = canvasWidthInPx, y = inputCableEnd.y)
                drawLine(
                    start = dividerStart,
                    end = dividerEnd,
                    color = copperColor,
                    strokeWidth = 4f
                )

                val dividerLength = dividerEnd.x - dividerStart.x

                var verticalLineStart = dividerStart
                var verticalLineEnd = Offset(x = verticalLineStart.x, y = verticalLineStart.y + 50)

                var circuitsStart = mutableListOf<Offset>()

                while (verticalLineStart.x <= dividerEnd.x) {
                    drawLine(
                        start = verticalLineStart,
                        end = verticalLineEnd,
                        color = copperColor,
                        strokeWidth = 4f
                    )
                    circuitsStart.add(verticalLineEnd)
                    verticalLineStart = Offset(x = verticalLineStart.x + dividerLength / 5, y = verticalLineStart.y)
                    verticalLineEnd = Offset(x = verticalLineStart.x, y= verticalLineStart.y + 50)
                }


                for (circuitStart in circuitsStart) {
                    val circuitEnd = Offset(x = circuitStart.x, y = canvasHeightInPx - 100)
                    drawLine(
                        start = circuitStart,
                        end = circuitEnd,
                        color = copperColor,
                        strokeWidth = 4f
                    )
                    drawCircle(
                        color = Color.Red,
                        radius = circleRadius,
                        center = Offset(x = circuitEnd.x, y = circuitEnd.y + 50)
                    )
                }

                if (outputCircuitsTextLayoutResult != null) {
                    drawText(outputCircuitsTextLayoutResult, topLeft = Offset(x = 0f, y= canvasHeightInPx/2))
                }
            }


        }
    }

}