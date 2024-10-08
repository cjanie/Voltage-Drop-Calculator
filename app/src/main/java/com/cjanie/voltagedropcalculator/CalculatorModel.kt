package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.Conductor
import com.cjanie.voltagedropcalculator.businesslogic.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.Length
import com.cjanie.voltagedropcalculator.businesslogic.Line
import com.cjanie.voltagedropcalculator.businesslogic.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.Material
import com.cjanie.voltagedropcalculator.businesslogic.Section
import com.cjanie.voltagedropcalculator.businesslogic.Tension
import com.cjanie.voltagedropcalculator.businesslogic.VoltageDrop

enum class LineType {
    SINGLE_PHASE, THREE_PHASE
}

class CalculatorModel {

    val functionalContextValues: Set<FunctionalContext> = FunctionalContext.values().toHashSet()

    val lineTypeValues: Set<LineType> = LineType.values().toHashSet()

    val conductorValues: Set<Material> = Material.values().toHashSet()

    val sectionValues: Set<Section> = setOf(
        Section(inMillimeterSquare = 1.5f),
        Section(inMillimeterSquare = 2.5f),
        Section(inMillimeterSquare = 4f),
        Section(inMillimeterSquare = 6f),
        Section(inMillimeterSquare = 10f),
        Section(inMillimeterSquare = 16f),
        Section(inMillimeterSquare = 25f),
        Section(inMillimeterSquare = 35f),
        Section(inMillimeterSquare = 50f),
        Section(inMillimeterSquare = 70f),
        Section(inMillimeterSquare = 95f),
        Section(inMillimeterSquare = 120f),
        Section(inMillimeterSquare = 150f),
        Section(inMillimeterSquare = 185f),
        Section(inMillimeterSquare = 240f),
        Section(inMillimeterSquare = 300f)
    )

    val currentIntensityValues: Set<Intensity> = setOf(
        Intensity(inAmpere = 20f),
        Intensity(inAmpere =100f),
        Intensity(inAmpere =150f),
        Intensity(inAmpere =500f)
    )

    val tensionValues: Set<Tension> = setOf(
        Tension(inVolt = 3f),
        Tension(24f),
        Tension(230f),
    )

    private var functionalContext: FunctionalContext? = null
    private var lineType: LineType? = null
    private var conductor: Conductor? = null
    private var section: Section? = null
    private var intensity: Intensity? = null
    private var tension: Tension? = null
    private var length: Length? = null

    private var line: Line? = null

    fun setFunctionalContext(itemPosition: Int) {
        functionalContext = functionalContextValues.toList()[itemPosition]
    }

    fun setLineType(itemPosition: Int) {
        lineType = lineTypeValues.toList()[itemPosition]
    }

    fun setConductor(itemPosition: Int) {
        conductor = Conductor(conductorValues.toList()[itemPosition])
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

    fun setLength(inKilometer: Float) {
        length = Length(inKilometer)
    }

    private fun initLine() {
        if (functionalContext == null ||
            lineType == null ||
            conductor == null ||
            section == null ||
            intensity == null ||
            length == null)
            throw NullValueException()

        line = createLine(
            functionalContext!!,
            lineType!!,
            conductor!!,
            section!!,
            intensity!!,
            length!!
        )
    }

    var voltageDrop: VoltageDrop? = null

    fun calculateVoltageDrop(): VoltageDrop? {
        if (length == null) throw NullValueException()
        if (line == null) initLine()
        voltageDrop = line?.voltageDrop()
        return voltageDrop
    }

    fun calculateVoltageDropInPercentage(): Float? {
        if(tension == null) throw NullValueException()
        if (voltageDrop == null) calculateVoltageDrop()
        return voltageDrop?.percentage(tension!!)
    }

    fun isVoltageDropAcceptable(): Boolean? {
        if (functionalContext == null) throw NullValueException()
        return calculateVoltageDropInPercentage()!! < functionalContext!!.maxVoltageDropPercentageAcceptable
    }

    fun maxVoltageDropPercentageAcceptable(): Float? {
        return functionalContext?.maxVoltageDropPercentageAcceptable
    }

    fun isNullValue(): Boolean {
        return functionalContext == null ||
                lineType == null ||
                conductor == null ||
                section == null ||
                intensity == null ||
                tension == null ||
                length == null
    }



    private fun createLine(
        functionalContext: FunctionalContext,
        lineType: LineType,
        conductor: Conductor,
        section: Section,
        intensity: Intensity,
        length: Length

    ): Line {
        return when(lineType) {
            LineType.SINGLE_PHASE -> LineSinglePhase(
                functionalContext = functionalContext,
                conductor = conductor,
                S = section,
                I = intensity,
                L = length
            )
            LineType.THREE_PHASE -> LineThreePhase(
                functionalContext = functionalContext,
                conductor = conductor,
                S = section,
                I = intensity,
                L = length
            )
        }
    }

}