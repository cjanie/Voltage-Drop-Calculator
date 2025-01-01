package com.cjanie.voltagedropcalculator.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.adapters.device.Light
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.Line
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.ConductorVoltageDropProperty
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.VoltageDrop

class VoltageDropViewModel: ViewModel() {

    // Device
    private val device = Light(consumes = Intensity(inAmpere = 20f))
    val usage = device.usage()
    val deviceIconId = when (usage) {
        Usage.LIGHTING -> R.drawable.ic_lightbulb_outline_24
        Usage.MOTOR -> R.drawable.ic_auto_mode_24
    }
    val intensityText = "${device.consumes().inAmpere} ${Intensity.unit}"

    // Conductor
    private val conductorMaterial = ConductorMaterial.COPPER
    val materialText = conductorMaterial.toString()
    private val S = Section(inMillimeterSquare = 2.5f)
    val sectionUnit = Section.unit
    val sectionText = "${S.inMillimeterSquare} $sectionUnit"


    // Voltage Drop at end of line
    private val K = ConductorVoltageDropProperty.property(
        conductorMaterial = conductorMaterial,
        section = S,
        usage = usage
    )
    private val I = device.consumes()

    var L = Length(inKilometer = 0.02f)

    fun delta_U(L: Length): VoltageDrop {
        return Line.voltageDropAtEndOfLine(K, I, L)
    }

    val lengthUnit = Length.unit
    fun lengthText(L: Length): String {
        return "${L.inKilometer} $lengthUnit"
    }

    val voltageDropUnit = VoltageDrop.unit

    fun voltageDropText(): String {
        return "${delta_U(L).inVolt} $voltageDropUnit"
    }

}