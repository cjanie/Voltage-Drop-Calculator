package com.cjanie.voltagedropcalculator.businesslogic.factories

import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Conductor
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Copper
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial

class ConductorFactory {

    companion object {
        fun conductor(conductorMaterial: ConductorMaterial): Conductor {
            return when (conductorMaterial) {
                ConductorMaterial.COPPER -> Copper()
            }
        }
    }
}