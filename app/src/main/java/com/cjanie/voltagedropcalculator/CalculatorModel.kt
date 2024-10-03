package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.Copper
import com.cjanie.voltagedropcalculator.businesslogic.Current
import com.cjanie.voltagedropcalculator.businesslogic.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.Line
import com.cjanie.voltagedropcalculator.businesslogic.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.LineThreePhase

enum class LineType {
    SINGLE_PHASE, THREE_PHASE
}
enum class Conductor {
    COPPER
}

class CalculatorModel {

    val functionalContextValues: Set<FunctionalContext> = FunctionalContext.values().toHashSet()

    val lineTypeValues: Set<LineType> = LineType.values().toHashSet()

    val conductorValues: Set<Conductor> = Conductor.values().toHashSet()

    val sectionValues: Set<Float> = setOf(
        1.5f,
        2.5f,
        4f,
        6f,
        10f,
        16f,
        25f,
        35f,
        50f,
        70f,
        95f,
        120f,
        150f,
        185f,
        240f,
        300f
    )

    val currentIntensityValues: Set<Float> = setOf(
        20f,
        100f,
        150f,
        500f

    )

    val tensionValues: Set<Float> = setOf(
        3f,
        24f,
        230f,
    )


    var functionalContext: FunctionalContext? = null
    var lineType: LineType? = null
    var conductor: Conductor? = null
    var section: Float? = null
    var intensity: Float? = null
    var tension: Float? = null
    var lineLength: Float? = null

    fun setFunctionnalContext(itemPosition: Int) {
        functionalContext = functionalContextValues.toList()[itemPosition]
    }

    fun setLineType(itemPosition: Int) {
        lineType = lineTypeValues.toList()[itemPosition]
    }

    fun setConductor(itemPosition: Int) {
        conductor = conductorValues.toList()[itemPosition]
    }

    fun setSection(itemPosition: Int) {
        section = sectionValues.toList()[itemPosition]
    }

    fun setIntensity(itemPosition: Int) {
        intensity = currentIntensityValues.toList()[itemPosition]
    }

    fun setTension(itemPosition: Int) {
        tension = tensionValues.toList()[itemPosition]
    }

    fun calculateVoltageDropInVolt(): Float {
        if (isNullValue()) throw NullValueException()
        return calculateVoltageDropInVolt(
            functionalContext!!,
            lineType!!,
            conductor!!,
            section!!,
            intensity!!,
            tension!!,
            lineLength!!
        )
    }

    fun isNullValue(): Boolean {
        return functionalContext == null ||
                lineType == null ||
                conductor == null ||
                section == null ||
                intensity == null ||
                tension == null ||
                lineLength == null
    }
    fun calculateVoltageDropInPercentage(): Float {
        if (isNullValue()) throw NullValueException()
        return calculateVoltageDropInPercentage(
            functionalContext!!,
            lineType!!,
            conductor!!,
            section!!,
            intensity!!,
            tension!!,
            lineLength!!
        )
    }

    private fun calculateVoltageDropInVolt(
        functionalContext: FunctionalContext,
        lineType: LineType,
        conductor: Conductor,
        section: Float,
        intensity: Float,
        tension: Float,
        lineLength: Float
    ): Float {
        val line = createLine(
            functionalContext,
            lineType,
            conductor,
            section,
            intensity,
            tension
        )
        return line.voltageDropInVolt(lineLength)
    }

    private fun createLine(
        functionalContext: FunctionalContext,
        lineType: LineType,
        conductor: Conductor,
        section: Float,
        intensity: Float,
        tension: Float
    ): Line {
        return when(lineType) {
            LineType.SINGLE_PHASE -> LineSinglePhase(
                conductor = when(conductor) {
                    Conductor.COPPER -> Copper(section)
                },
                current = Current(intensity),
                functionalContext = functionalContext,
                tensionNominalInVolt = tension
            )
            LineType.THREE_PHASE -> LineThreePhase(
                conductor = when(conductor) {
                    Conductor.COPPER -> Copper(section)
                },
                current = Current(intensity),
                functionalContext = functionalContext,
                tensionNominalInVolt = tension
            )
        }
    }

    private fun calculateVoltageDropInPercentage(
        functionalContext: FunctionalContext,
        lineType: LineType,
        conductor: Conductor,
        section: Float,
        intensity: Float,
        tension: Float,
        lineLength: Float
    ): Float {
        val line : Line = createLine(
            functionalContext,
            lineType,
            conductor,
            section,
            intensity,
            tension
        )
        return line.voltageDropInPercentage(lineLength)
    }

    fun isVoltageDropAcceptable(): Boolean {
        if (isNullValue()) throw NullValueException()
        val line : Line = createLine(
            functionalContext!!,
            lineType!!,
            conductor!!,
            section!!,
            intensity!!,
            tension!!
        )
        return line.isVoltageDropAcceptable(lineLength!!)
    }
}