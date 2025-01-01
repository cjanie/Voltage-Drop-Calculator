package com.cjanie.voltagedropcalculator.businesslogic.models.conductor

import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.PhaseShift
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section

class Resistance(
    conductor: Conductor,
    section: Section,
    phaseShift: PhaseShift,
    phasingRatio: Float) {

    private val linearResistance = conductor.linearResistance(section)
    private val linearReactance = conductor.linearReactance
    val inOhmPerKilometer = (linearResistance.inOhmPerKilomer * phaseShift.cosPHI + linearReactance.inOhmPerKilomer * phaseShift.sinPhi) * phasingRatio

}