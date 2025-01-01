package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.Copper
import org.junit.Assert.assertEquals
import org.junit.Test

class ConductorVoltageDropPropertyTest {

    @Test
    fun voltage_drop_copper_section_1_5() {
        // Conducteur en cuivre
        // section 1.5 mm2
        // lighting
        // K = 32 // Delta_U in volt per Ampere and per Kimometer
        val section = Section(inMillimeterSquare = 1.5f)
        val K = Copper.voltageDropProperty(section, usage = Usage.LIGHTING)
        assertEquals(31.6f, K?.inVoltPerAmpereAndPerKilometer)
    }

    @Test
    fun voltage_drop_copper_section_2_5() {
        // Conducteur en cuivre
        // section 2.5 mm2
        // lighting
        // K = 19 // Delta_U in volt per Ampere and per Kimometer
        val section = Section(inMillimeterSquare = 2.5f)
        val K = Copper.voltageDropProperty(section, usage = Usage.LIGHTING)
        assertEquals(18.960001f, K?.inVoltPerAmpereAndPerKilometer)
    }

    @Test
    fun voltage_drop_copper_section_4() {
        // Conducteur en cuivre
        // section 4 mm2
        // lighting
        // K = 11.9 // Delta_U in volt per Ampere and per Kimometer
        val section = Section(inMillimeterSquare = 4f)
        val K = Copper.voltageDropProperty(section, usage = Usage.LIGHTING)
        assertEquals(11.85f, K?.inVoltPerAmpereAndPerKilometer)
    }
}