package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.ui.composables.commons.Label
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationViewModel
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.paddingMedium
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationSetUpStep

class Pin(val start: Offset, val end: Offset)
class Switch(val start: Offset, val end: Offset)

@Composable
fun InstallationDrawing(installationPresenter: InstallationViewModel.InstallationPresenter, editionMode: Boolean, modifier: Modifier = Modifier.fillMaxSize()) {

    Column(modifier = modifier)
         {

        //InstallationSpecifications(installationPresenter)

        InstallationCanvas(
            installationPresenter = installationPresenter,
            editionMode = editionMode,
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
    installationPresenter: InstallationViewModel.InstallationPresenter,
    editionMode: Boolean,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    var columnHeightInPx by remember {
        mutableFloatStateOf(0f)
    }
    var columnWidthInPx by remember {
        mutableFloatStateOf(0f)
    }

    val editIcon = ImageVector.vectorResource(id = R.drawable.baseline_edit_24)
    val iconPainter = rememberVectorPainter(image = editIcon)

    var editStep: InstallationSetUpStep? by remember {
        mutableStateOf(null)
    }

    if (editStep != null) {
        Text(text = editStep.toString())
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
        val supplierText = installationPresenter.electricitySupply
        val tensionText = installationPresenter.tension
        val inputCableText = installationPresenter.inputCablePresenter.cableText
        val outputCircuitsText = installationPresenter.outputCircuitsPresenter?.cableText
        val usageText = installationPresenter.functionalContext
        val style = TextStyle(
            fontSize = 16.sp,
            color = Color.Black
        )
        val supplierTextLayoutResult = remember(supplierText, style) {
            textMeasurer.measure(supplierText, style)
        }

        val tensionTextResultLayout = remember(tensionText, style) {
            textMeasurer.measure(tensionText, style)
        }
        val inputCableTextLayoutResult = remember(inputCableText, style) {
            textMeasurer.measure(inputCableText, style)
        }
        val outputCircuitsTextLayoutResult = remember(outputCircuitsText, style) {
            if(outputCircuitsText != null) {
                textMeasurer.measure(outputCircuitsText, style)
            } else null
        }
        val usageTextResultLayout = remember(usageText, style) {
            textMeasurer.measure(usageText, style)
        }


        val horizontalPadding = paddingMedium
        val verticalPadding = paddingMedium
        

        var canvasWidthInPx by remember {
            mutableFloatStateOf(0f)
        }

        var canvasHeightInPx by remember {
            mutableFloatStateOf(0f)
        }



        Canvas(
            modifier = modifier
                .padding(horizontalPadding, verticalPadding)
                .pointerInput(true) {
                    detectTapGestures(onTap = { offset ->
                       if(offset.x > canvasWidthInPx - canvasWidthInPx / 4) {
                            if (offset.y < canvasHeightInPx / 8)
                                editStep = InstallationSetUpStep.DEFINE_NOMINAL_TENSION

                            else if (offset.y > canvasHeightInPx / 8 + (canvasHeightInPx - canvasHeightInPx/8) / 2) {
                                editStep = if(offset.y > canvasHeightInPx - canvasHeightInPx / 16)
                                    InstallationSetUpStep.DEFINE_USAGE
                                else InstallationSetUpStep.ADD_OUTPUT_CIRCUITS
                            } else editStep = InstallationSetUpStep.ADD_INPUT_CABLE
                       }
                    })
                }
        ) {

            canvasWidthInPx = columnWidthInPx - horizontalPadding.toPx() * 2
            canvasHeightInPx = columnHeightInPx - verticalPadding.toPx() * 2

            val strokeWidth = 4f

            var supplierHeight = canvasHeightInPx / 8
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

            val pinHeight = canvasHeightInPx / 32
            val supplierPinStart = supplierContactPoints.find { it.x != supplierCirclesCenterX }!!
            val supplierPinEnd = Offset(supplierPinStart.x, supplierPinStart.y + pinHeight)
            drawLine(color = copperColor, supplierPinStart, supplierPinEnd, strokeWidth)

            val supplierPinTip = supplierPinEnd
            val pinTipOffset = pinHeight / 2
            drawLine(
                start = Offset(supplierPinTip.x - pinTipOffset, supplierPinTip.y + pinTipOffset),
                end = Offset(supplierPinTip.x + pinTipOffset, supplierPinTip.y - pinTipOffset),
                color = copperColor,
                strokeWidth = 4f
            )
            drawLine(
                start = Offset(supplierPinTip.x + pinTipOffset, supplierPinTip.y + pinTipOffset),
                end = Offset(supplierPinTip.x - pinTipOffset, supplierPinTip.y - pinTipOffset),
                color = copperColor,
                strokeWidth = 4f
            )

            val switchHeight = pinHeight * 2
            val switch = Switch(
                    start = Offset(
                        x = supplierPinTip.x - pinTipOffset * 2,
                        y = supplierPinTip.y + pinTipOffset
                    ),
                    end = Offset(
                        x = supplierPinTip.x,
                        y = supplierPinTip.y + switchHeight
                    )
                )
            drawLine(copperColor, switch.start, switch.end, 4f)

            // Repartitor
            val dividerWidth = supplierWidth + supplierWidth / 16
            val dividerY = supplierHeight + (canvasHeightInPx - supplierHeight) / 2
            val dividerStart = Offset(x = canvasWidthInPx - dividerWidth, y = dividerY)
            val dividerEnd = Offset(x = canvasWidthInPx, y = dividerY)

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

            if (installationPresenter.inputCablePresenter.phasing == Phasing.THREE_PHASE) {
                val cableMiddleBefore = Offset(x = inputCableMiddle.x, y = inputCableMiddle.y - inputCableHeight / 16)
                val cableMiddleAfter = Offset(x = inputCableMiddle.x, y = inputCableMiddle.y + inputCableHeight / 16)
                val phasingSignCenters = listOf(cableMiddleBefore, inputCableMiddle, cableMiddleAfter)

                for (center in phasingSignCenters) {
                    val phasingLinesOffset = canvasWidthInPx / 32
                    drawLine(
                        color = copperColor,
                        start = Offset(x = center.x - phasingLinesOffset, y = center.y + phasingLinesOffset),
                        end = Offset(x = center.x + phasingLinesOffset, y = center.y - phasingLinesOffset)
                    )
                }
            }



            // Terminal
            val deviceCircleRadius = canvasHeightInPx / 32

            if (installationPresenter.outputCircuitsPresenter == null) {
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
                var pinEnd = Offset(x = pinStart.x, y = pinStart.y + pinHeight)




                var pins = mutableListOf<Pin>()

                while (pinStart.x <= dividerEnd.x) {
                    val pin = Pin(pinStart, pinEnd)
                    pins.add(pin)

                    pinStart = Offset(x = pinStart.x + dividerLength / 4, y = pinStart.y)
                    pinEnd = Offset(x = pinStart.x, y= pinStart.y + pinHeight)
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
                        start = Offset(pinTip.x - pinTipOffset, pinTip.y + pinTipOffset),
                        end = Offset(pinTip.x + pinTipOffset, pinTip.y - pinTipOffset),
                        color = copperColor,
                        strokeWidth = 4f
                    )
                    drawLine(
                        start = Offset(pinTip.x + pinTipOffset, pinTip.y + pinTipOffset),
                        end = Offset(pinTip.x - pinTipOffset, pinTip.y - pinTipOffset),
                        color = copperColor,
                        strokeWidth = 4f
                    )
                }



                val switches = Array(3) {
                    Switch(
                        start = Offset(
                            x = pinTipsForElectricContacts[it].x - pinTipOffset * 2,
                            y = pinTipsForElectricContacts[it].y + pinTipOffset
                        ),
                        end = Offset(
                            x = pinTipsForElectricContacts[it].x,
                            y = pinTipsForElectricContacts[it].y + switchHeight
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
                    val circleCenter = Offset(x = circuitEnd.x, y = canvasHeightInPx - deviceCircleRadius)
                    drawCircle(
                        color = copperColor,
                        radius = deviceCircleRadius,
                        center = circleCenter,
                        style = Stroke(strokeWidth)
                    )
                    val trigonometryResult = deviceCircleRadius // TODO
                    drawLine(
                        start = Offset(circleCenter.x - trigonometryResult, circleCenter.y + trigonometryResult),
                        end = Offset(circleCenter.x + trigonometryResult, circleCenter.y - trigonometryResult),
                        color = copperColor,
                        strokeWidth = 4f
                    )
                    drawLine(
                        start = Offset(circleCenter.x + trigonometryResult, circleCenter.y + trigonometryResult),
                        end = Offset(circleCenter.x - trigonometryResult, circleCenter.y - trigonometryResult),
                        color = copperColor,
                        strokeWidth = 4f
                    )

                }

                // Legends

                val supplierTextY = 0f
                val tensionTextY = supplierCircleBottomCenterY
                val usageTextY = circuitEndY + deviceCircleRadius - iconPainter.intrinsicSize.height / 2


                drawText(
                    supplierTextLayoutResult,
                    topLeft = Offset(
                        x = canvasWidthInPx - iconPainter.intrinsicSize.width * 2 - supplierTextLayoutResult.size.width,
                        y = supplierTextY)
                )
                drawText(
                    tensionTextResultLayout,
                    topLeft = Offset(
                        x = canvasWidthInPx - iconPainter.intrinsicSize.width * 2 - supplierTextLayoutResult.size.width,
                        y = tensionTextY)
                )


                drawText(
                    inputCableTextLayoutResult,
                    topLeft = Offset(
                        x = canvasWidthInPx - iconPainter.intrinsicSize.width * 2 - inputCableTextLayoutResult.size.width,
                        y = inputCableMiddle.y - inputCableTextLayoutResult.size.height / 2))


                val circuitsHeight = circuitEndY - circuitsStart[0].y
                val circuitsMiddleY = circuitsStart[0].y + circuitsHeight / 2

                if (outputCircuitsTextLayoutResult != null) {
                    drawText(
                        outputCircuitsTextLayoutResult,
                        topLeft = Offset(
                            x = canvasWidthInPx - iconPainter.intrinsicSize.width * 2 - outputCircuitsTextLayoutResult.size.width,
                            y= circuitsMiddleY - outputCircuitsTextLayoutResult.size.height / 2)
                    )
                }
                drawText(usageTextResultLayout, topLeft = Offset(
                    x = canvasWidthInPx - iconPainter.intrinsicSize.width * 2 - usageTextResultLayout.size.width,
                    y = usageTextY
                    )
                )

                // Edition
                val editButtonX = canvasWidthInPx - iconPainter.intrinsicSize.width
                val supplierButtonY = tensionTextY / 2 - iconPainter.intrinsicSize.height / 2
                val inputCableButtonY = inputCableMiddle.y - iconPainter.intrinsicSize.height / 2

                val circuitsButtonY = circuitsMiddleY - iconPainter.intrinsicSize.height / 2
                val usageButtonY = usageTextY
                if(editionMode) {
                    showEditionMode(
                        drawScope = this,
                        editButtonX = editButtonX,
                        editButtonYCoordinates = arrayOf(
                            supplierButtonY,
                            inputCableButtonY,
                            circuitsButtonY,
                            usageButtonY
                        ),
                        painter = iconPainter
                    )
                }

            }
        }
    }
}

fun showEditionMode(drawScope: DrawScope, editButtonX: Float, editButtonYCoordinates: Array<Float>, painter: VectorPainter) {
    for (editButtonY in editButtonYCoordinates) {
        drawEditButton(
            drawScope = drawScope,
            left = editButtonX,
            top = editButtonY,
            painter = painter
        )
    }

}

fun drawEditButton(drawScope: DrawScope, left: Float, top: Float, painter: VectorPainter) {
    drawScope.translate(left = left, top = top) {
        with(painter) {
            draw(painter.intrinsicSize)
        }
    }
}