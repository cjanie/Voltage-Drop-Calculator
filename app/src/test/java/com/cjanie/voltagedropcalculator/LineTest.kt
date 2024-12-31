package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.Line
import org.junit.Assert.assertEquals
import org.junit.Test


class LineTest {

    @Test
    fun voltageDropLighting() {
        // circuit 20 m
        // copper 2.5 mm2
        // I = 20 A
        val S = Section(inMillimeterSquare = 2.5f)
        val I = Intensity(inAmpere = 20f)
        val L = Length(inKilometer = 0.02f)

        val delta_U = Line(length = L, section = S, intensity = I).voltageDrop()
        assertEquals(7.584f, delta_U.inVolt)
    }
}