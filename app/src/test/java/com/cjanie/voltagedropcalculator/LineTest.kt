package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.adapters.device.Light
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.Line
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.ConductorVoltageDropProperty
import org.junit.Assert.assertEquals
import org.junit.Test


class LineTest {

    @Test
    fun voltageDropAtEndOfLineLighting() {
        // circuit 20 m
        // copper 2.5 mm2
        // I = 20 A
        val S = Section(inMillimeterSquare = 2.5f)
        val K = ConductorVoltageDropProperty.property(
            conductorMaterial = ConductorMaterial.COPPER,
            section = S,
            usage = Usage.LIGHTING
        )
        val I = Intensity(inAmpere = 20f)
        val L = Length(inKilometer = 0.02f)

        val delta_U = Line.voltageDropAtEndOfLine(K, I, L)

        assertEquals(7.584f, delta_U.inVolt)
    }
}