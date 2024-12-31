package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.ui.DrawingTools
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.theme.paddingMedium
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.TruncatedInstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.TruncatedInstallationSetUpViewModel

class Pin(val start: Offset, val end: Offset)
class Switch(val start: Offset, val end: Offset)
enum class InstallationTemplate {
    COMPLETE, TRUNCATED
}
@Composable
fun InstallationDrawing(
    completeInstallationPresenter: CompleteInstallationSetUpViewModel.CompleteInstallationPresenter,
    truncatedInstallationPresenter: TruncatedInstallationSetUpViewModel.TruncatedInstallationPresenter,
    editionMode: Boolean,
    installationTemplate: InstallationTemplate = InstallationTemplate.TRUNCATED,
    setInstallationSetUpStep: (step: CompleteInstallationSetUpStep) -> Unit,
    setTruncatedInstallationSetUpStep: (step: TruncatedInstallationSetUpStep) -> Unit,
    modifier: Modifier = Modifier.fillMaxSize()
) {

    // Edit icon for edition mode
    val editIcon = ImageVector.vectorResource(id = R.drawable.baseline_edit_24)
    val editIconPainter = rememberVectorPainter(image = editIcon)

    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        fontSize = 16.sp,
        color = Color.Black
    )

    val strokeWidth = 4f

    when (installationTemplate) {
        InstallationTemplate.COMPLETE -> CompleteInstallationCanvas(
            completeInstallationPresenter = completeInstallationPresenter,
            editionMode = editionMode,
            editIconPainter = editIconPainter,
            textMeasurer = textMeasurer,
            textStyle = textStyle,
            setInstallationSetUpStep = setInstallationSetUpStep,
            modifier = modifier,
            strokeWidth = strokeWidth
        )

        InstallationTemplate.TRUNCATED -> TruncatedInstallationCanvas(
            installationPresenter = truncatedInstallationPresenter,
            isEditionMode = editionMode,
            editIconPainter = editIconPainter,
            textMeasurer = textMeasurer,
            textStyle = textStyle,
            modifier = modifier,
            strokeWidth = strokeWidth,
            setInstallationSetUpStep = setTruncatedInstallationSetUpStep
        )
    }
}

@Composable
fun TruncatedInstallationCanvas(
    installationPresenter: TruncatedInstallationSetUpViewModel.TruncatedInstallationPresenter,
    isEditionMode: Boolean,
    editIconPainter: VectorPainter,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    modifier: Modifier = Modifier.fillMaxSize(),
    horizontalPadding: Dp = paddingMedium,
    verticalPadding: Dp = paddingMedium,
    strokeWidth: Float,
    setInstallationSetUpStep: (step: TruncatedInstallationSetUpStep) -> Unit
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
        var canvasWidthInPx by remember {
            mutableFloatStateOf(0f)
        }

        var canvasHeightInPx by remember {
            mutableFloatStateOf(0f)
        }

        val electricitySupplyText = installationPresenter.electricitySupply
        val nominalTensionText = installationPresenter.tension
        val inputCableVoltageDropText = installationPresenter.inputCableVoltageDrop
        val outputCableText = installationPresenter.outputCircuitsPresenter?.cableText
        val usageText = installationPresenter.usageAsString

        var electricitySupplyTextLayoutResult = remember(electricitySupplyText, textStyle) {
            textMeasurer.measure(electricitySupplyText, textStyle)
        }

        var nominalTensionTextLayoutResult = remember(nominalTensionText, textStyle) {
            textMeasurer.measure(nominalTensionText, textStyle)
        }

        var inputCableVoltageDropTextLayoutResult = remember(inputCableVoltageDropText, textStyle) {
            textMeasurer.measure(inputCableVoltageDropText, textStyle)
        }

        var circuitCableTextlayoutResult = remember(outputCableText, textStyle) {
            textMeasurer.measure(outputCableText!!, textStyle)
        }

        var usageTextLayoutResult = remember(usageText, textStyle) {
            textMeasurer.measure(usageText, textStyle)
        }

        var electricitySupplyClickableEndY by remember {
            mutableFloatStateOf(0f)
        }

        var tensionClickableEndY by remember {
            mutableFloatStateOf(0f)
        }

        var inputCableVoltageDropEndY by remember {
            mutableFloatStateOf(0f)
        }

        var circuitCableClickableEndY by remember {
            mutableFloatStateOf(0f)
        }

        var usageClickableEndY by remember {
            mutableFloatStateOf(0f)
        }

        val canvasClickableModifier = modifier
            .padding(horizontalPadding, verticalPadding)
            .pointerInput(true) {
                detectTapGestures(onTap = { offset ->

                    if (offset.x > canvasWidthInPx - canvasWidthInPx / 4) {
                        if (offset.y < electricitySupplyClickableEndY)
                            setInstallationSetUpStep(
                                TruncatedInstallationSetUpStep.DEFINE_ELECTRICITY_SUPPLY
                            )
                        else if (offset.y < tensionClickableEndY) {
                            setInstallationSetUpStep(
                                TruncatedInstallationSetUpStep.DEFINE_NOMINAL_TENSION
                            )
                        }
                        else if (offset.y < inputCableVoltageDropEndY) {
                            setInstallationSetUpStep(
                                TruncatedInstallationSetUpStep.DEFINE_INPUT_CABLE_VOLTAGE_DROP
                            )
                        }
                        else if (offset.y < circuitCableClickableEndY) {
                            setInstallationSetUpStep(
                                TruncatedInstallationSetUpStep.ADD_OUTPUT_CIRCUITS
                            )
                        }
                        else setInstallationSetUpStep(TruncatedInstallationSetUpStep.DEFINE_USAGE)
                    }
                })
            }

        Canvas(
            modifier = if(isEditionMode) canvasClickableModifier
                else modifier.padding(horizontalPadding, verticalPadding)
        ) {

            canvasWidthInPx = columnWidthInPx - horizontalPadding.toPx() * 2
            canvasHeightInPx = columnHeightInPx - verticalPadding.toPx() * 2

            val inputCableStart = Offset(x = canvasWidthInPx / 3, y = 0f)
            val inputCableHeight = canvasHeightInPx / 3
            DrawingTools.drawCable(
                drawScope = this,
                start = inputCableStart,
                height = inputCableHeight,
                strokeWidth = strokeWidth
            )

            val deviceCircleRadius = canvasHeightInPx / 32
            val circuitsRepartitorStart = Offset(x = 0f + deviceCircleRadius, y = inputCableHeight)
            val circuitsRepartitorWidth = canvasWidthInPx * 3 / 4
            DrawingTools.drawCircuitsRepartitor(
                drawScope = this,
                start = circuitsRepartitorStart,
                width = circuitsRepartitorWidth,
                strokeWidth = strokeWidth
            )

            val pinsX = mutableListOf<Float>()

            val lapBetweenPins = circuitsRepartitorWidth / 4

            var pinStartX = circuitsRepartitorStart.x

            while (pinStartX <= circuitsRepartitorWidth + deviceCircleRadius) {
                pinsX.add(pinStartX)
                pinStartX = pinStartX + lapBetweenPins
            }

            val pinHeight = canvasHeightInPx / 64

            val pinsStart = Array(pinsX.size) {
                Offset(x = pinsX[it], y = circuitsRepartitorStart.y)
            }

            DrawingTools.drawPins(
                this,
                pinsStart = pinsStart,
                height = pinHeight,
                strokeWidth = strokeWidth

            )

            val inputCableContactPoint = Offset(x = inputCableStart.x, y = inputCableStart.y + inputCableHeight)

            val contactPoints = mutableListOf(inputCableContactPoint)

            for (i in pinsStart.indices) {
                if(i > 0 && i < pinsStart.size - 1)
                    contactPoints.add(pinsStart[i])
            }

            val pointRadius = strokeWidth * 2

            DrawingTools.drawContactPoints(
                drawScope = this,
                contactPoints = contactPoints.toTypedArray(),
                radius = pointRadius
            )

            val pinTip = Offset(x = pinsX[0], y = circuitsRepartitorStart.y + pinHeight)
            val pinTipOffset = pinHeight / 2

            DrawingTools.drawPinTip(
                drawScope = this,
                pinTip = pinTip,
                pinTipOffset = pinTipOffset,
                strokeWidth = strokeWidth
            )

            val switchStart = Offset(x = pinTip.x, y = pinTip.y + pinTipOffset)
            val switchOffset = pinTipOffset * 2
            val switchHeight = canvasHeightInPx / 16
            DrawingTools.drawSwitch(
                drawScope = this,
                start = switchStart,
                startOffset = switchOffset,
                height = switchHeight,
                strokeWidth = strokeWidth
            )

            val circuitCableStart = Offset(x = switchStart.x, y = pinTip.y + switchHeight + pinTipOffset)

            val motorSwitchStart = Offset(x = circuitCableStart.x, y = canvasHeightInPx - deviceCircleRadius * 3 - switchHeight)

            val circuitCableHeight = motorSwitchStart.y - circuitCableStart.y

            DrawingTools.drawCable(
                drawScope = this,
                start = circuitCableStart,
                height = circuitCableHeight,
                strokeWidth = strokeWidth
            )

            val motorContactradius = switchOffset / 2
            val motorContactTopLeft = Offset(motorSwitchStart.x - motorContactradius / 2, motorSwitchStart.y - motorContactradius / 2)

            DrawingTools.drawMotorContact(
                this,
                radius = motorContactradius,
                topLeft = motorContactTopLeft,
                strokeWidth = strokeWidth
            )

            DrawingTools.drawSwitch(
                drawScope = this,
                start = motorSwitchStart,
                startOffset = switchOffset,
                height = switchHeight,
                strokeWidth = strokeWidth,
            )
            val motorCableStart = Offset(x= motorSwitchStart.x, motorSwitchStart.y + switchHeight)
            val motorCableHeight = canvasHeightInPx - (deviceCircleRadius * 2 + motorSwitchStart.y + switchHeight)
            DrawingTools.drawCable(
                this,
                start = motorCableStart,
                height = motorCableHeight,
                strokeWidth = strokeWidth
            )


            val deviceCircleCenter = Offset(circuitCableStart.x, canvasHeightInPx - deviceCircleRadius)

            DrawingTools.drawMotor(
                drawScope = this,
                deviceCircleRadius = deviceCircleRadius,
                circleCenter = deviceCircleCenter
            )


            /*

            DrawingTools.drawLight(
                this,
                deviceCircleRadius = deviceCircleRadius,
                circleCenter = deviceCircleCenter,
                strokeWidth = strokeWidth
            )
            */

            val electricitySupplyClickableStartY = inputCableStart.y
            val electricitySupplyClickableHeight = inputCableHeight / 3
            electricitySupplyClickableEndY = electricitySupplyClickableStartY + electricitySupplyClickableHeight
            val electricitySypplyTextY = electricitySupplyClickableStartY + electricitySupplyClickableHeight / 2

            val tensionClickableStartY = electricitySupplyClickableStartY + electricitySupplyClickableHeight
            val tensionClickableHeight = inputCableHeight / 3
            tensionClickableEndY = tensionClickableStartY + tensionClickableHeight
            val tensionTextY = tensionClickableStartY + tensionClickableHeight / 2

            val inputCableVoltageDropClickableStartY = tensionClickableStartY + tensionClickableHeight
            val inputCableVoltageDropClickableHeight = inputCableHeight / 3
            inputCableVoltageDropEndY = inputCableVoltageDropClickableStartY + inputCableVoltageDropClickableHeight
            val inputCableVoltageDropTextY = inputCableVoltageDropClickableStartY + inputCableVoltageDropClickableHeight / 2

            val circuitCableClickableStartY = inputCableVoltageDropClickableStartY + inputCableVoltageDropClickableHeight
            val circuitCableClickableHeight = circuitCableHeight + switchHeight + pinHeight + pinTipOffset
            circuitCableClickableEndY = circuitCableClickableStartY + circuitCableClickableHeight
            val circuitCableTextY = circuitCableClickableStartY + circuitCableClickableHeight / 2

            val usageClickableStartY = circuitCableClickableStartY + circuitCableClickableHeight
            val usageClickableHeight = switchHeight + motorCableHeight + deviceCircleRadius * 2
            usageClickableEndY = usageClickableStartY + usageClickableHeight
            val usageTextY = usageClickableStartY + usageClickableHeight / 2

            val textMap = mapOf(
                electricitySypplyTextY to electricitySupplyTextLayoutResult,
                tensionTextY to nominalTensionTextLayoutResult,
                inputCableVoltageDropTextY to inputCableVoltageDropTextLayoutResult,
                circuitCableTextY to circuitCableTextlayoutResult,
                usageTextY to usageTextLayoutResult
            )
            val colorMap = mapOf(
                circuitCableTextY to installationPresenter.outputCircuitsPresenter?.textColor
            )
            DrawingTools.drawLegends(
                this,
                canvasWidthInPx = canvasWidthInPx,
                verticalCoordinatesOfLegendsMap = textMap,
                textColorMap = colorMap
            )

            if(isEditionMode) {

                DrawingTools.drawEditionMode(
                    drawScope = this,
                    canvasWidthInPx = canvasWidthInPx,
                    editButtonYCoordinates = arrayOf(
                        electricitySypplyTextY,
                        tensionTextY,
                        inputCableVoltageDropTextY,
                        circuitCableTextY,
                        usageTextY
                    ),
                    painter = editIconPainter
                )
            }
        }
    }
}
