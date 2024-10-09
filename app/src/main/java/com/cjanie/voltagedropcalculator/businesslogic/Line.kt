package com.cjanie.voltagedropcalculator.businesslogic

import kotlin.math.cos
import kotlin.math.sin

abstract class Line (
    private val functionalContext: FunctionalContext,
    private val conductor: Conductor,
    S: Section,
    private val I: Intensity,
    private val nominal_U: Tension,
    private val L: Length
) {
    private val R = this.conductor.RESISTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH(S)
    private val X = this.conductor.REACTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH()
    private val PHI = this.functionalContext.phi

    private val phaseShift = R * cos(PHI) + X * sin(PHI)

    protected abstract fun phaseShiftRatio(): Float

    private val K = this.phaseShiftRatio() * phaseShift

    private val voltageDrop = voltageDrop()

    protected open fun voltageDrop(): VoltageDrop {
        return VoltageDrop( inVolt = K * I.inAmpere * L.inKilometer)
    }

    fun voltageDropInVolt(): Float {
        return voltageDrop.inVolt
    }

    fun voltageDropPercentage(): Float {
        return voltageDrop.percentage(nominal_U)
    }

    fun isVoltageDropAcceptable(): Boolean {
        return voltageDrop.isAcceptable(nominal_U, functionalContext)
    }

    fun maxVoltageDropAcceptablePercentage(): Float {
        return functionalContext.maxVoltageDropPercentageAcceptable
    }

    // Max length acceptable
    open fun maxLengthAcceptable(): Length {
        val delta_U_inVolt = functionalContext.maxVoltageDropPercentageAcceptable * nominal_U.inVolt / 100
        return Length(inKilometer = delta_U_inVolt / K * I.inAmpere)
    }

}