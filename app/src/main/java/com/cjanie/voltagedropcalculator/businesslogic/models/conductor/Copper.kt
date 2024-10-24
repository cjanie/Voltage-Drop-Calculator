package com.cjanie.voltagedropcalculator.businesslogic.models.conductor

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial

class Copper: Conductor() {
    override val material: ConductorMaterial
        get() = ConductorMaterial.COPPER
    override val resistanceReference: ResistanceReference
        get() = ResistanceReference(inOhmByMillimeterSquarePerKilomer = 23.7f)
}