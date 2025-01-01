package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.gateways.Device
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section

class LineFactory {

    companion object {
        fun line(length: Length, conductorMaterial: ConductorMaterial = ConductorMaterial.COPPER, section: Section, device: Device): Line {
            return Line(
                length = length,
                section = section,
                conductorMaterial = conductorMaterial,
                intensity = device.consumes(),
                usage = device.usage()
            )
        }
    }


}