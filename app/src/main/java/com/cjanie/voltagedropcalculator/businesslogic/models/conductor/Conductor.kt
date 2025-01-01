package com.cjanie.voltagedropcalculator.businesslogic.models.conductor

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section

abstract class Conductor(val linearReactance: LinearReactance = defaultLinearReactance) {

    class ResistanceReference(val inOhmByMillimeterSquarePerKilomer: Float)
    class LinearResistance(val inOhmPerKilomer: Float)
    class LinearReactance(val inOhmPerKilomer: Float)

    abstract val material: ConductorMaterial
    protected abstract val resistanceReference: ResistanceReference

    companion object {
        private val defaultLinearReactance = LinearReactance(inOhmPerKilomer = 0.08f)
    }

    fun linearResistance(section: Section): LinearResistance {
        return LinearResistance(
            inOhmPerKilomer = resistanceReference.inOhmByMillimeterSquarePerKilomer / section.inMillimeterSquare
        )
    }
}