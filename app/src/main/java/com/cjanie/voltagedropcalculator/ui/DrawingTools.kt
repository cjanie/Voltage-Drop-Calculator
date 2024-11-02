package com.cjanie.voltagedropcalculator.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.VectorPainter
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

        fun drawEditionMode(drawScope: DrawScope, canvasWidthInPx: Float, editButtonYCoordinates: Array<Float>, painter: VectorPainter) {
            val editButtonX = canvasWidthInPx - painter.intrinsicSize.width
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

    }
}