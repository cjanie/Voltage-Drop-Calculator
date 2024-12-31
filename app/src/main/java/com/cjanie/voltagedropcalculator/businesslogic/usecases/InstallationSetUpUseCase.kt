package com.cjanie.voltagedropcalculator.businesslogic.usecases

import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.businesslogic.PhaseShiftInconsistancyException
import com.cjanie.voltagedropcalculator.businesslogic.models.CompleteInstallation
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Use
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage

class InstallationSetUpUseCase(val use: Use, val voltage: Voltage) {

    private var cable: Line? = null

    fun addInput(cable: Line) {

        if (!Installation.isPhaseShiftConsistent(cable, use.usage, use.electricitySupply))
            throw PhaseShiftInconsistancyException()
        else this.cable = cable

    }

    fun addOutput(circuits: Array<Line>) {
        if (cable == null) throw NullValueException()
        for(circuit in circuits) {
            if (!Installation.isPhaseShiftConsistent(circuit, use.usage, use.electricitySupply)) throw PhaseShiftInconsistancyException()
        }

        cable?.supplies(circuits)

    }

    fun getInstallation(): CompleteInstallation? {
        if (cable != null) {
            return CompleteInstallation(
                use = use,
                input = cable!!,
                output = cable!!.output,
                nominalVoltage = voltage
            )
        } else return null
    }

}