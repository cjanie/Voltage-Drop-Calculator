package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.ui.DrawingTools
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationViewModel
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.paddingMedium
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationSetUpStep

class Pin(val start: Offset, val end: Offset)
class Switch(val start: Offset, val end: Offset)

@Composable
fun InstallationDrawing(
        installationPresenter: InstallationViewModel.InstallationPresenter,
        editionMode: Boolean,
        setInstallationSetUpStep: (step: InstallationSetUpStep) -> Unit,
        modifier: Modifier = Modifier.fillMaxSize()
) {

    Column(modifier = modifier)
         {
        InstallationCanvas(
            installationPresenter = installationPresenter,
            editionMode = editionMode,
            setInstallationSetUpStep = setInstallationSetUpStep,
            modifier = modifier
        )


    }
}

@Composable
fun InstallationCanvas(
    installationPresenter: InstallationViewModel.InstallationPresenter,
    editionMode: Boolean,
    setInstallationSetUpStep: (step: InstallationSetUpStep) -> Unit,
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
        val usageText = installationPresenter.usageAsString
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


        val canvasClickableModifier = modifier
            .padding(horizontalPadding, verticalPadding)
            .pointerInput(true) {
                detectTapGestures(onTap = { offset ->

                    if(offset.x > canvasWidthInPx - canvasWidthInPx / 4) {
                        if (offset.y < canvasHeightInPx / 8)
                            setInstallationSetUpStep(if (offset.y < canvasHeightInPx / 16)
                                InstallationSetUpStep.DEFINE_ELECTRICITY_SUPPLY
                            else InstallationSetUpStep.DEFINE_NOMINAL_TENSION
                            )

                        else if (offset.y > canvasHeightInPx / 8 + (canvasHeightInPx - canvasHeightInPx/8) / 2) {
                            setInstallationSetUpStep(
                                if(offset.y > canvasHeightInPx - canvasHeightInPx / 16)
                                    InstallationSetUpStep.DEFINE_USAGE
                                else InstallationSetUpStep.ADD_OUTPUT_CIRCUITS
                            )
                        } else setInstallationSetUpStep(InstallationSetUpStep.ADD_INPUT_CABLE)
                    }
                })
            }
        Canvas(
            modifier = if (editionMode) canvasClickableModifier else modifier
                .padding(horizontalPadding, verticalPadding)
        ) {

            canvasWidthInPx = columnWidthInPx - horizontalPadding.toPx() * 2
            canvasHeightInPx = columnHeightInPx - verticalPadding.toPx() * 2

            val strokeWidth = 4f

            var supplierHeight = canvasHeightInPx / 8
            val supplierWidth = canvasWidthInPx - canvasWidthInPx / 4
            drawLine(copperColor, Offset(x = 0f, y = supplierHeight), Offset(x = supplierWidth, y = supplierHeight), strokeWidth)
            val supplierCirclesCenterX = canvasWidthInPx / 4

            val supplierContactPoints = arrayOf(
                Offset(x = supplierCirclesCenterX, y = supplierHeight),
                Offset(x = supplierWidth / 3 * 2, y = supplierHeight)
            )

            val pinHeight = canvasHeightInPx / 32

            DrawingTools.drawElectricitySupplier(this,
                supplierCirclesCenterX = supplierCirclesCenterX,
                supplierHeight = supplierHeight,
                supplierContactPoints = supplierContactPoints,
                strokeWidth = strokeWidth
            )


            val supplierPinStart = supplierContactPoints.find { it.x != supplierCirclesCenterX }!!
            val supplierPinEnd = Offset(supplierPinStart.x, supplierPinStart.y + pinHeight)
            drawLine(color = copperColor, supplierPinStart, supplierPinEnd, strokeWidth)

            val pinTipOffset = pinHeight / 2
            DrawingTools.drawPinTip(
                this,
                pinTip = supplierPinEnd,
                pinTipOffset = pinTipOffset,
                strokeWidth = strokeWidth
            )

            val switchHeight = pinHeight * 2
            val switch = Switch(
                    start = Offset(
                        x = supplierPinEnd.x - pinTipOffset * 2,
                        y = supplierPinEnd.y + pinTipOffset
                    ),
                    end = Offset(
                        x = supplierPinEnd.x,
                        y = supplierPinEnd.y + switchHeight
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

                val pins = mutableListOf<Pin>()

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
                    DrawingTools.drawPinTip(this,
                        pinTip = pinTip,
                        pinTipOffset = pinTipOffset,
                        strokeWidth = strokeWidth
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

                    val deviceCircleCenter = Offset(x = circuitEnd.x, y = canvasHeightInPx - deviceCircleRadius)

                    when (installationPresenter.usage) {
                        FunctionalContext.LIGHTING -> DrawingTools.drawLight(this,
                            deviceCircleRadius = deviceCircleRadius,
                            circleCenter = deviceCircleCenter,
                            strokeWidth = strokeWidth
                        )
                        FunctionalContext.MOTOR -> DrawingTools.drawMotor(this,
                            deviceCircleRadius = deviceCircleRadius,
                            circleCenter = deviceCircleCenter
                        )
                    }

                }

                // Legends
                val supplierTextY = supplierHeight / 4
                val tensionTextY = supplierHeight - supplierHeight / 4
                val usageTextY = circuitEndY + deviceCircleRadius
                val inputTextY = inputCableMiddle.y
                val circuitsHeight = circuitEndY - circuitsStart[0].y
                val circuitsMiddleY = circuitsStart[0].y + circuitsHeight / 2
                val outputTextY = circuitsMiddleY

                val verticalCoordinatesOfLegendsMap = mapOf(
                    supplierTextY to supplierTextLayoutResult,
                    tensionTextY to tensionTextResultLayout,
                    inputTextY to inputCableTextLayoutResult,
                    outputTextY to outputCircuitsTextLayoutResult,
                    usageTextY to usageTextResultLayout
                )

                DrawingTools.drawLegends(this,
                    canvasWidthInPx = canvasWidthInPx,
                    verticalCoordinatesOfLegendsMap = verticalCoordinatesOfLegendsMap)

                // Edition
                val supplierButtonY = supplierTextY
                val tensionButtonY = tensionTextY
                val inputCableButtonY = inputCableMiddle.y - iconPainter.intrinsicSize.height / 2
                val circuitsButtonY = circuitsMiddleY - iconPainter.intrinsicSize.height / 2
                val usageButtonY = usageTextY

                if(editionMode) {
                    DrawingTools.drawEditionMode(
                        drawScope = this,
                        canvasWidthInPx = canvasWidthInPx,
                        editButtonYCoordinates = arrayOf(
                            supplierButtonY,
                            tensionButtonY,
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
