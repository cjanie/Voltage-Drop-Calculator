package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Copper
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import junit.framework.TestCase.assertEquals


import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class VoltageDropInVoltPerAmpereAndKilometerTest {

    fun voltageDropInVoltPerAmpereKilometer(section: Section): Float {
        val R = 23.7f / section.inMillimeterSquare
        val X = 0.08f
        val cosPhi = 1f
        val sinPhi = PhaseShift(cosPhi).sinPhi
        assertEquals(0f, sinPhi)

        val voltageDropInVoltPerAmpereKilometer = 2 * (R * cosPhi + X * sinPhi)
        val decimal = BigDecimal(voltageDropInVoltPerAmpereKilometer.toDouble()).setScale(2, RoundingMode.HALF_EVEN)

        return decimal.toFloat()
    }

    val map : Map<Float, Float> = mapOf(
        1.5f to 32f,
        2.5f to 19f,
        4f to 11.9f
    )

    @Test
    fun cable_copper_singleLine_Lighting() {
        for (section in map.keys) {
            assertEquals(map.get(section), voltageDropInVoltPerAmpereKilometer(Section(section)))
        }
    }

    @Test
    fun cable_copper_singleLine_35_milimeterSquare_lighting() {
        val section = Section(35f)
        val conductor = Copper()
        val voltageDropInVolt = 1.35f

        val R = 23.7f / section.inMillimeterSquare
        val X = 0.08f
        val cosPhi = 1f

        val K = voltageDropInVoltPerAmpereKilometer(section)
        assertEquals(1.35f, K)
    }

    @Test
    fun cable_copper_singleline_50_milimeterSquare_lighting() {
        val section = Section(50f)
        val K = voltageDropInVoltPerAmpereKilometer(section)
        assertEquals(1.0f, K)
    }

    @Test
    fun cable_copper_singleline_70_milimeterSquare_lighting() {
        val section = Section(70f)
        val K = voltageDropInVoltPerAmpereKilometer(section)
        assertEquals(0.68f, K)
    }
}