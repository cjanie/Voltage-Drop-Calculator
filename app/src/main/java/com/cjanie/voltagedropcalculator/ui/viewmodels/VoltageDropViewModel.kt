package com.cjanie.voltagedropcalculator.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.adapters.device.Light
import com.cjanie.voltagedropcalculator.adapters.voltageprovider.VoltageProvider230
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.Line
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.ConductorVoltageDropProperty
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.VoltageDrop
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.redWarningColor

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

    var nominal_U = Voltage(inVolt = 230f)

    fun isVoltageDropAcceptable(): Boolean {
        val delta_U = delta_U(I, L)
        val percentage = delta_U.percentage(nominal_U = nominal_U)
        val maxPercentageAcceptable = when (usage) {
            Usage.LIGHTING -> 4.4f
            Usage.MOTOR -> 6f
        }
        return percentage <= maxPercentageAcceptable
    }

    fun warningColor(): Color {
        return if (isVoltageDropAcceptable()) greenWarningColor else redWarningColor
    }

    fun infoText(): String {
        val delta_U = delta_U(I, L)
        val voltageDropPercentage = delta_U.percentage(nominal_U)
        return "$voltageDropPercentage from ${nominal_U.inVolt} ${Voltage.unit} Max 4.4 %"
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