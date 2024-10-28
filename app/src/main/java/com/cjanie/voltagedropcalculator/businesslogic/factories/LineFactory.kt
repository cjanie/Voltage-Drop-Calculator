package com.cjanie.voltagedropcalculator.businesslogic.factories

import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Use
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length

class LineFactory {

    companion object {
        fun line(functionalContext: FunctionalContext, electricitySupply: ElectricitySupply, phasing: Phasing, conductorMaterial: ConductorMaterial, section: Section, intensity: Intensity, length: Length): Line {

            val use: Use = when(functionalContext) {
                FunctionalContext.LIGHTING -> Lighting(electricitySupply = electricitySupply)
                FunctionalContext.MOTOR -> Lighting(electricitySupply = electricitySupply) // TODO MOTOR
            }

            val conductor = ConductorFactory.conductor(conductorMaterial)

            return when(phasing) {
                Phasing.SINGLE_PHASE -> LineSinglePhase(
                    phaseShift = use.phaseShift,
                    conductor = conductor,
                    section = section,
                    intensity = intensity,
                    length = length
                )
                Phasing.THREE_PHASE -> LineThreePhase(
                    phaseShift = use.phaseShift,
                    conductor = conductor,
                    section = section,
                    intensity = intensity,
                    length = length
                )
            }
        }
    }

}