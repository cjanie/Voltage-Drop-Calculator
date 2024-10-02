package com.cjanie.voltagedropcalculator.businesslogic

import kotlin.math.cos
import kotlin.math.sin

abstract class Line(
    private val conductor: Conductor,
    private val current: Current,
    private val functionalContext: FunctionalContext,
    private val lengthInKilometer: Float
) {

    private val I = this.current.intensityInAmpere

    private val L = this.lengthInKilometer
    private val R = this.conductor.RESISTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH()
    private val X = this.conductor.REACTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH()

    private val PHI: Float by lazy {
        when(this.functionalContext) {
            FunctionalContext.LIGHTING -> 0f
        }
    }

    private val phaseShift = R * cos(PHI) + X * sin(PHI)

    protected abstract fun phaseShiftRatio(): Float

    private val K = this.phaseShiftRatio() * phaseShift


    open fun calculateVoltageDropInVolt(): Float {
        return K * I * L
    }

}