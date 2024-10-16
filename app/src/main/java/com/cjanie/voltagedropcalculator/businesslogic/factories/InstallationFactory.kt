package com.cjanie.voltagedropcalculator.businesslogic.factories

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionnalContext
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Use
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension

class InstallationFactory {

    companion object {
        fun installation(functionnalContext: FunctionnalContext, electricitySupply: ElectricitySupply, cable: Line, circuits: Array<Line> = emptyArray(), nominalTension: Tension): Installation {

            val use: Use = when(functionnalContext) {
                FunctionnalContext.LIGHTING -> Lighting(electricitySupply = electricitySupply)
            }

            return Installation(use = use, cable = cable, circuits = circuits, nominalTension = nominalTension)
        }
    }

}