package com.cjanie.voltagedropcalculator.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import com.cjanie.voltagedropcalculator.ui.theme.copperColor

class DrawingTools {

    companion object {

        fun drawElectricitySupplier(
            drawScope: DrawScope,
            supplierCirclesCenterX: Float,
            supplierHeight: Float,
            supplierContactPoints: Array<Offset>,
            strokeWidth: Float,
            color: Color = copperColor
        ) {
            val supplierCircleTopCenterY = supplierHeight / 4
            val supplierCircleBottomCenterY = supplierHeight / 4 * 2
            val supplierCirclesRadius = supplierHeight / 4
            drawScope.drawCircle(
                color = color,
                center = Offset(x = supplierCirclesCenterX, y = supplierCircleTopCenterY),
                radius = supplierCirclesRadius,
                style = Stroke(strokeWidth)
            )
            drawScope.drawCircle(
                color = color,
                center = Offset(x = supplierCirclesCenterX, y = supplierCircleBottomCenterY),
                radius = supplierCirclesRadius,
                style = Stroke(strokeWidth)
            )

            drawScope.drawLine(
                color = color,
                start = Offset(x = supplierCirclesCenterX, y = supplierCircleBottomCenterY + supplierCirclesRadius),
                end = supplierContactPoints.find { it.x == supplierCirclesCenterX }!!,
                strokeWidth = strokeWidth
            )

            for (point in supplierContactPoints) {
                drawScope.drawCircle(
                    color = copperColor,
                    center = point,
                    radius = strokeWidth * 2
                )
            }



        }

        fun drawPinTip(drawScope: DrawScope, pinTip: Offset, pinTipOffset: Float, color: Color = copperColor, strokeWidth: Float) {
            drawScope.drawLine(
                start = Offset(pinTip.x - pinTipOffset, pinTip.y + pinTipOffset),
                end = Offset(pinTip.x + pinTipOffset, pinTip.y - pinTipOffset),
                color = color,
                strokeWidth = strokeWidth
            )
            drawScope.drawLine(
                start = Offset(pinTip.x + pinTipOffset, pinTip.y + pinTipOffset),
                end = Offset(pinTip.x - pinTipOffset, pinTip.y - pinTipOffset),
                color = color,
                strokeWidth = strokeWidth
            )
        }

        fun drawLight(drawScope: DrawScope, deviceCircleRadius: Float, circleCenter: Offset, strokeWidth: Float, color: Color = copperColor) {
            drawScope.drawCircle(
                color = color,
                radius = deviceCircleRadius,
                center = circleCenter,
                style = Stroke(strokeWidth)
            )
            val trigonometryResult = deviceCircleRadius // TODO
            drawScope.drawLine(
                start = Offset(circleCenter.x - trigonometryResult, circleCenter.y + trigonometryResult),
                end = Offset(circleCenter.x + trigonometryResult, circleCenter.y - trigonometryResult),
                color = color,
                strokeWidth = strokeWidth
            )
            drawScope.drawLine(
                start = Offset(circleCenter.x + trigonometryResult, circleCenter.y + trigonometryResult),
                end = Offset(circleCenter.x - trigonometryResult, circleCenter.y - trigonometryResult),
                color = color,
                strokeWidth = strokeWidth
            )
        }

        fun drawMotor(
            drawScope: DrawScope,
            deviceCircleRadius: Float,
            circleCenter: Offset,
            color: Color = copperColor
        ) {

            drawScope.drawCircle(
                color = color,
                radius = deviceCircleRadius,
                center = circleCenter
            )

            val path = Path()
            path.moveTo(x = circleCenter.x, y = circleCenter.y)
            path.lineTo(circleCenter.x + deviceCircleRadius, y= circleCenter.y + deviceCircleRadius)
            path.lineTo(x = circleCenter.x - deviceCircleRadius, y = circleCenter.y + deviceCircleRadius)
            path.close()
            drawScope.drawPath(path, color)
        }

        fun drawPins(drawScope: DrawScope, pinsStart: Array<Offset>, height: Float, strokeWidth: Float, color: Color = copperColor) {
            for (pinStart in pinsStart) {
                drawPin(drawScope, start = pinStart, height = height, strokeWidth = strokeWidth, color = color)
            }

        }

        fun drawPin(drawScope: DrawScope, start: Offset, height: Float, strokeWidth: Float, color: Color = copperColor) {
            val end = Offset(x =start.x, y = start.y + height)
            drawScope.drawLine(start = start, end = end, strokeWidth = strokeWidth, color = color)
        }

        fun drawContactPoints(drawScope: DrawScope, contactPoints: Array<Offset>, radius : Float, color: Color = copperColor) {
            for (point in contactPoints) drawScope.drawCircle(color = color, center = point, radius = radius)
        }

        fun drawSwitch(drawScope: DrawScope, start: Offset, startOffset: Float, height: Float, strokeWidth: Float, color: Color = copperColor) {
            val startWithOffset = Offset(x= start.x - startOffset, y = start.y)
            val end = Offset(x = start.x, y = startWithOffset.y + height)
            drawScope.drawLine(copperColor, start= startWithOffset, end= end, strokeWidth= strokeWidth)
        }

        fun drawMotorContact(drawScope: DrawScope, radius: Float, topLeft: Offset, strokeWidth: Float, color: Color = copperColor) {
            drawScope.drawArc(
                color = color,
                startAngle = 90f,
                sweepAngle = 180f,
                useCenter =  true,
                size = Size(radius, radius),
                topLeft = topLeft,
                style = Stroke(strokeWidth)
            )
        }

        fun drawCable(drawScope: DrawScope, start: Offset, height: Float, strokeWidth: Float, color: Color = copperColor) {
            val end = Offset(start.x, start.y + height)
            drawScope.drawLine(color = color, start = start, end = end, strokeWidth = strokeWidth)
        }

        fun drawCircuitsRepartitor(drawScope: DrawScope, start: Offset, width: Float, strokeWidth: Float, color: Color = copperColor) {
            val end = Offset(start.x + width, start.y)
            drawScope.drawLine(color = color, start = start, end = end, strokeWidth = strokeWidth)
        }


        fun drawLegends(
            drawScope: DrawScope,
            canvasWidthInPx: Float,
            verticalCoordinatesOfLegendsMap: Map<Float, TextLayoutResult?>,
            textColorMap: Map<Float, Color?>
        ) {
            val textX = canvasWidthInPx - canvasWidthInPx / 4

            for (coordinateY in verticalCoordinatesOfLegendsMap.keys) {
                val textLayoutResult = verticalCoordinatesOfLegendsMap.get(coordinateY)
                if (textLayoutResult != null) {
                    drawScope.drawText(
                        verticalCoordinatesOfLegendsMap.get(coordinateY)!!,
                        color = if (textColorMap.get(coordinateY) != null)
                                textColorMap.get(coordinateY)!!
                            else Color.Unspecified,
                        topLeft = Offset(
                            x = textX,
                            y = coordinateY - textLayoutResult.size.height / 2
                        )

                    )
                }
            }
        }

        fun drawEditionMode(drawScope: DrawScope, canvasWidthInPx: Float, editButtonYCoordinates: Array<Float>, painter: VectorPainter) {
            val editButtonX = canvasWidthInPx - painter.intrinsicSize.width

            for (editButtonY in editButtonYCoordinates) {
               drawEditButton(
                    drawScope = drawScope,
                    left = editButtonX,
                    top = editButtonY - painter.intrinsicSize.width / 2,
                    painter = painter
                )
            }
        }

       private fun drawEditButton(drawScope: DrawScope, left: Float, top: Float, painter: VectorPainter) {
            drawScope.translate(left = left, top = top) {
                with(painter) {
                    draw(painter.intrinsicSize)
                }
            }
       }

    }
}