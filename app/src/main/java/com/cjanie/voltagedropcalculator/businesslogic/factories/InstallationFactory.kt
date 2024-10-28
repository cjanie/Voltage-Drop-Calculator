package com.cjanie.voltagedropcalculator.businesslogic.factories

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Use
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension

class InstallationFactory {

    companion object {
        fun installation(functionalContext: FunctionalContext, electricitySupply: ElectricitySupply, cable: Line, nominalTension: Tension): Installation {

            val use: Use = when(functionalContext) {
                FunctionalContext.LIGHTING -> Lighting(electricitySupply = electricitySupply)
                FunctionalContext.MOTOR -> Lighting(electricitySupply = electricitySupply) // TODO MOTOR IMPL
            }

            return Installation(use = use, input = cable, output = cable.output, nominalTension = nominalTension)
        }
    }

}