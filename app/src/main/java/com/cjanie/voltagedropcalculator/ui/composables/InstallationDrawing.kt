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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.ui.composables.commons.Label
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationViewModel
import com.cjanie.voltagedropcalculator.ui.theme.copperColor

class Pin(val start: Offset, val end: Offset)
class Switch(val start: Offset, val end: Offset)

@Composable
fun InstallationDrawing(installationPresenter: InstallationViewModel.InstallationPresenter, modifier: Modifier = Modifier.fillMaxSize()) {

    Column(modifier = modifier)
         {

        //InstallationSpecifications(installationPresenter)

        InstallationCanvas(
            inputCablePresenter = installationPresenter.inputCablePresenter,
            outputCircuitsPresenter = installationPresenter.outputCircuitsPresenter,
            modifier = modifier
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
    outputCircuitsPresenter: InstallationViewModel.CablePresenter?,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    var columnHeightInPx by remember {
        mutableFloatStateOf(0f)
    }
    var columnWidthInPx by remember {
        mutableFloatStateOf(0f)
    }

    Column(
        modifier = modifier
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
        val verticalPadding = 20.dp


        Canvas(modifier = modifier.padding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)) {
            val canvasWidthInPx = columnWidthInPx - horizontalPadding.toPx() * 2
            val canvasHeightInPx = columnHeightInPx - verticalPadding.toPx() * 2

            val strokeWidth = 4f

            val supplierHeight = canvasHeightInPx / 8
            val supplierWidth = canvasWidthInPx - canvasWidthInPx / 4
            drawLine(copperColor, Offset(x = 0f, y = supplierHeight), Offset(x = supplierWidth, y = supplierHeight), strokeWidth)
            val supplierCirclesCenterX = canvasWidthInPx / 4
            val supplierCircleTopCenterY = supplierHeight / 4
            val supplierCircleBottomCenterY = supplierHeight / 4 * 2
            val supplierCirclesRadius = supplierHeight / 4
            drawCircle(
                color = copperColor,
                center = Offset(x = supplierCirclesCenterX, y = supplierCircleTopCenterY),
                radius = supplierCirclesRadius,
                style = Stroke(strokeWidth)
            )
            drawCircle(
                color = copperColor,
                center = Offset(x = supplierCirclesCenterX, y = supplierCircleBottomCenterY),
                radius = supplierCirclesRadius,
                style = Stroke(strokeWidth)
            )

            val supplierContactPoints = listOf(
                Offset(x = supplierCirclesCenterX, y = supplierHeight),
                Offset(x = supplierWidth / 3 * 2, y = supplierHeight)
            )
            drawLine(
                color = copperColor,
                start = Offset(x = supplierCirclesCenterX, y = supplierCircleBottomCenterY + supplierCirclesRadius),
                end = supplierContactPoints.find { it.x == supplierCirclesCenterX }!!,
                strokeWidth = strokeWidth
            )

            for (point in supplierContactPoints) {
                drawCircle(
                    color = copperColor,
                    center = point,
                    radius = strokeWidth * 2
                )
            }

            val supplierPinHeight = 50f
            val supplierPinStart = supplierContactPoints.find { it.x != supplierCirclesCenterX }!!
            val supplierPinEnd = Offset(supplierPinStart.x, supplierPinStart.y + supplierPinHeight)
            drawLine(color = copperColor, supplierPinStart, supplierPinEnd, strokeWidth)

            val supplierPinTip = supplierPinEnd
            drawLine(
                start = Offset(supplierPinTip.x - 20, supplierPinTip.y + 20),
                end = Offset(supplierPinTip.x + 20, supplierPinTip.y - 20),
                color = copperColor,
                strokeWidth = 4f
            )
            drawLine(
                start = Offset(supplierPinTip.x + 20, supplierPinTip.y + 20),
                end = Offset(supplierPinTip.x - 20, supplierPinTip.y - 20),
                color = copperColor,
                strokeWidth = 4f
            )

            val switch = Switch(
                    start = Offset(
                        x = supplierPinTip.x - 40,
                        y = supplierPinTip.y + 20
                    ),
                    end = Offset(
                        x = supplierPinTip.x,
                        y = supplierPinTip.y + 100
                    )
                )
            drawLine(copperColor, switch.start, switch.end, 4f)

            // Repartitor
            val dividerWidth = supplierWidth + supplierWidth / 16
            val dividerStart = Offset(x = canvasWidthInPx - dividerWidth, y = canvasHeightInPx / 2)
            val dividerEnd = Offset(x = canvasWidthInPx, y = canvasHeightInPx / 2)

            // Draw input cable
            val inputCableStart = switch.end
            val inputCableEnd = Offset(x = inputCableStart.x, y = dividerStart.y)
            val inputCableHeight = inputCableEnd.y - inputCableStart.y
            drawLine(
                start = inputCableStart,
                end = inputCableEnd,
                color = copperColor,
                strokeWidth = 4f
            )

            val inputCableMiddle = Offset(x = inputCableEnd.x, y = inputCableEnd.y - inputCableHeight / 2)

            if (inputCablePresenter.phasing == Phasing.THREE_PHASE) {
                val cableMiddleBefore = Offset(x = inputCableMiddle.x, y = inputCableMiddle.y - inputCableHeight / 16)
                val cableMiddleAfter = Offset(x = inputCableMiddle.x, y = inputCableMiddle.y + inputCableHeight / 16)
                val phasingSignCenters = listOf(cableMiddleBefore, inputCableMiddle, cableMiddleAfter)

                for (center in phasingSignCenters) {
                    drawLine(
                        color = copperColor,
                        start = Offset(x = center.x - 40, y = center.y + 40),
                        end = Offset(x = center.x + 40, y = center.y - 40),
                        strokeWidth = strokeWidth
                    )
                }
            }

            // Input cable legend
            drawText(inputCableTextLayoutResult, topLeft = Offset(x = 0f, y = inputCableMiddle.y))

            // Terminal
            val deviceCircleRadius = 50f

            if (outputCircuitsPresenter == null) {
                drawCircle(
                    color = Color.Red,
                    radius = deviceCircleRadius,
                    center = Offset(x = inputCableEnd.x, y = inputCableEnd.y + deviceCircleRadius)
                )
            } else {

                // Output circuits
                // Horizontal line

                drawLine(
                    start = dividerStart,
                    end = dividerEnd,
                    color = copperColor,
                    strokeWidth = 4f
                )

                val dividerLength = dividerEnd.x - dividerStart.x

                var pinStart = dividerStart
                var pinEnd = Offset(x = pinStart.x, y = pinStart.y + 50)




                var pins = mutableListOf<Pin>()

                while (pinStart.x <= dividerEnd.x) {
                    val pin = Pin(pinStart, pinEnd)
                    pins.add(pin)

                    pinStart = Offset(x = pinStart.x + dividerLength / 4, y = pinStart.y)
                    pinEnd = Offset(x = pinStart.x, y= pinStart.y + 50)
                }

                for (pin in pins) {
                    drawLine(
                        start = pin.start,
                        end = pin.end,
                        color = copperColor,
                        strokeWidth = 4f
                    )

                }

                val circuitContactPoints = listOf(
                    inputCableEnd,
                    pins[1].start,
                    pins[2].start,
                    pins[3].start
                )

                for (point in circuitContactPoints) {
                    drawCircle(
                        color = copperColor,
                        center = point,
                        radius = strokeWidth * 2
                    )
                }


                val pinTipsForElectricContacts = Array(3) {
                    pins[it].end
                }
                for (pinTip in pinTipsForElectricContacts) {
                    drawLine(
                        start = Offset(pinTip.x - 20, pinTip.y + 20),
                        end = Offset(pinTip.x + 20, pinTip.y - 20),
                        color = copperColor,
                        strokeWidth = 4f
                    )
                    drawLine(
                        start = Offset(pinTip.x + 20, pinTip.y + 20),
                        end = Offset(pinTip.x - 20, pinTip.y - 20),
                        color = copperColor,
                        strokeWidth = 4f
                    )
                }



                val switches = Array(3) {
                    Switch(
                        start = Offset(
                            x = pinTipsForElectricContacts[it].x - 40,
                            y = pinTipsForElectricContacts[it].y + 20
                        ),
                        end = Offset(
                            x = pinTipsForElectricContacts[it].x,
                            y = pinTipsForElectricContacts[it].y + 100
                        )
                    )
                }

                for (switch in switches) {
                    drawLine(copperColor, switch.start, switch.end, 4f)
                }


                val circuitsStart = switches.map { it.end }
                val circuitEndY = canvasHeightInPx - deviceCircleRadius * 2
                for (circuitStart in circuitsStart) {
                    val circuitEnd = Offset(x = circuitStart.x, y = circuitEndY)
                    drawLine(
                        start = circuitStart,
                        end = circuitEnd,
                        color = copperColor,
                        strokeWidth = 4f
                    )
                    drawCircle(
                        color = Color.Red,
                        radius = deviceCircleRadius,
                        center = Offset(x = circuitEnd.x, y = canvasHeightInPx - deviceCircleRadius)
                    )
                }

                if (outputCircuitsTextLayoutResult != null) {
                    val circuitHeight = circuitEndY - circuitsStart[0].y
                    val circuitMiddleY = circuitEndY - circuitHeight / 2
                    drawText(outputCircuitsTextLayoutResult, topLeft = Offset(x = 0f, y= circuitMiddleY))
                }
            }


        }
    }

}