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

    // Conductor
    private val conductorMaterial = ConductorMaterial.COPPER
    val materialText = conductorMaterial.toString()
    var S = Section(inMillimeterSquare = 2.5f)
    val sectionUnit = Section.unit
    fun sectionText(): String {
        return "$sectionUnit ${S.inMillimeterSquare}"
    }


    // Voltage Drop at end of line
    private fun K(): ConductorVoltageDropProperty {
        return ConductorVoltageDropProperty.property(
            conductorMaterial = conductorMaterial,
            section = S,
            usage = usage
        )
    }
    var I = device.consumes()

    var L = Length(inKilometer = 0.02f)

    fun delta_U(I: Intensity, L: Length): VoltageDrop {
        return Line.voltageDropAtEndOfLine(K(), I, L)
    }

    fun intensityText(): String {
        return "${Intensity.unit} ${I.inAmpere}"
    }

    val lengthUnit = Length.unit
    fun lengthText(): String {
        return "$lengthUnit ${L.inKilometer}"
    }

    val voltageDropUnit = VoltageDrop.unit

    fun voltageDropText(): String {
        return "$voltageDropUnit ${delta_U(I, L).inVolt}"
    }

}