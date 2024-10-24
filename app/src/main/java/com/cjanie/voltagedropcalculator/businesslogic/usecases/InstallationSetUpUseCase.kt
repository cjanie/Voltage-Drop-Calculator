package com.cjanie.voltagedropcalculator.businesslogic.usecases

import com.cjanie.voltagedropcalculator.businesslogic.PhaseShiftInconsistancyException
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Use
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension

class InstallationSetUpUseCase(val use: Use, val tension: Tension) {

    private var cable: Line? = null

    fun addInput(cable: Line) {
        if(cable != null) {
            if (cable.phaseShift == use.phaseShift)
                this.cable = cable
            else throw PhaseShiftInconsistancyException()
        }
    }

    fun addOutput(circuits: Array<Line>) {
        for(circuit in circuits) {
            if (circuit.phaseShift != use.phaseShift) throw PhaseShiftInconsistancyException()
        }
        if (cable != null) {
            cable?.supplies(circuits)
        }
    }

    fun getInstallation(): Installation? {
        if (cable != null) {
            return Installation(
                use = use,
                input = cable!!,
                output = cable!!.output,
                nominalTension = tension
            )
        } else return null
    }

}