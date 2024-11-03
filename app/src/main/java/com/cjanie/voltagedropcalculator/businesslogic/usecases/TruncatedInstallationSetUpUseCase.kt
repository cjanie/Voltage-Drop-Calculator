package com.cjanie.voltagedropcalculator.businesslogic.usecases

import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.businesslogic.models.TruncatedInstallation
import com.cjanie.voltagedropcalculator.businesslogic.PhaseShiftInconsistancyException
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Motor
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop

class TruncatedInstallationSetUpUseCase(
    private val usage: Usage,
    private val electricitySupply: ElectricitySupply,
    private val nominalTension: Tension,
    private val inputCableVoltageDrop: VoltageDrop
) {
    private val outputCircuits = mutableListOf<Line>()

    fun addOutputCircuit(outputCircuit: Line) {
        if (!Installation.isPhaseShiftConsistent(outputCircuit, usage, electricitySupply))
            throw PhaseShiftInconsistancyException()
        outputCircuits.add(outputCircuit)
    }

    fun getInstallation(): TruncatedInstallation {
        if(outputCircuits.isEmpty()) throw NullValueException()

        else return TruncatedInstallation(
            usage = usage,
            electricitySupply = electricitySupply,
            nominalTension = nominalTension,
            inputCableVoltageDrop = inputCableVoltageDrop!!,
            outputCircuits = outputCircuits.toTypedArray()
        )
    }
}