package com.cjanie.voltagedropcalculator.businesslogic.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Conductor

class Resistance(
    conductor: Conductor,
    section: Section,
    phaseShift: PhaseShift,
    phasingRatio: Float) {

    private val linearResistance = conductor.linearResistance(section)
    private val linearReactance = conductor.linearReactance
    val inOhmPerKilometer = (linearResistance.inOhmPerKilomer * phaseShift.cosPHI + linearReactance.inOhmPerKilomer * phaseShift.sinPhi) * phasingRatio

}