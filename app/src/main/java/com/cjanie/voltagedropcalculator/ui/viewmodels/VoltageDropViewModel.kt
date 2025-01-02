package com.cjanie.voltagedropcalculator.ui.viewmodels

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.DimensionUnit
import com.cjanie.voltagedropcalculator.businesslogic.enums.OhmLawParameterUnit
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.VoltageDropCalculator
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.VoltageDrop
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.redWarningColor

class VoltageDropViewModel(application: Application): AndroidViewModel(application) {

    val usage = Usage.LIGHTING

    // Conductor
    private val conductorMaterial = ConductorMaterial.COPPER
    var S = Section(inMillimeterSquare = 2.5f)

    // Voltage Drop at end of line
    var I = Intensity(inAmpere = 20f)

    var L = Length(inKilometer = 0.02f)

    private fun voltageDropAtEndOfLine(): VoltageDrop {
        return VoltageDropCalculator(
            conductorMaterial = conductorMaterial,
            S = S,
            usage = usage,
            I = I,
            L = L
        ).voltageDropAtEndOfLine
    }

    // acceptable depending on context nominal U and usage
    var nominal_U = Voltage(inVolt = 230f)

    private fun isVoltageDropAcceptable(): Boolean {
        return voltageDropAtEndOfLine().isVoltageDropAcceptable(nominal_U, usage)
    }


    // Device Icon
    val deviceIconId = when (usage) {
        Usage.LIGHTING -> R.drawable.ic_lightbulb_outline_24
        Usage.MOTOR -> R.drawable.ic_auto_mode_24
    }

    // Texts

    val app = getApplication<Application>()

    val materialText = conductorMaterial()

    val sectionUnit = Section.unit
    fun sectionText(): String {
        return "${dimensionUnit(sectionUnit)} ${S.inMillimeterSquare}"
    }
    val maxSection = Section(inMillimeterSquare = 300f)

    fun intensityText(): String {
        return "${unit(Intensity.unit)} ${I.inAmpere}"
    }
    val maxIntensity = Intensity(inAmpere = 1000f)

    val lengthUnit = Length.unit
    fun lengthText(): String {
        return "${dimensionUnit(lengthUnit)} ${L.inKilometer}"
    }
    val maxLength = Length(inKilometer = 1f)

    val voltageDropUnit = VoltageDrop.unit
    
    fun voltageDropText(): String {
        val label = app.getString(R.string.voltage_drop)
        return "${label} ${unit(voltageDropUnit)} ${voltageDropAtEndOfLine().inVolt}"
    }

    fun warningColor(): Color {
        return if (isVoltageDropAcceptable()) greenWarningColor else redWarningColor
    }

    fun nominalVoltageText(): String {

        return "${app.getString(R.string.nominal_voltage)} ${unit(Voltage.unit)} ${nominal_U.inVolt}"
    }
    val maxVoltage = Voltage(inVolt = 1000f)

    val percentageSign = app.getString(R.string.percentage_sign)

    fun voltageDropPercentageText(): String {
        val voltageDropPercentageLabel = "${app.getString(R.string.voltage_drop)} $percentageSign"
        val voltageDropPercentage = voltageDropAtEndOfLine().percentage(nominal_U)
        return "$voltageDropPercentageLabel  $voltageDropPercentage"
    }

    fun maxVoltageDropAcceptedText(): String {
        val maxVoltageDropLabel = "${usage()} ${app.getString(R.string.max_voltage_drop_accepted)} $percentageSign"
        return "$maxVoltageDropLabel ${VoltageDrop.maxPercentageAcceptable(usage)}"
    }

    fun conductorMaterial(): String {
        return when (conductorMaterial) {
            ConductorMaterial.COPPER -> app.getString(R.string.copper_abbrev)
        }
    }

    fun usage(): String {
        return when (usage) {
            Usage.LIGHTING -> app.getString(R.string.usage_lighting)
            Usage.MOTOR -> app.getString(R.string.usage_motor)
        }
    }

    // unit tools
    fun unit(parameterUnit: OhmLawParameterUnit): String {
        return when (parameterUnit) {
            OhmLawParameterUnit.OHM -> app.getString(R.string.ohm)
            OhmLawParameterUnit.VOLT -> app.getString(R.string.volt_abbrev)
            OhmLawParameterUnit.AMPERE -> app.getString(R.string.ampere_abbrev)
        }
    }

    fun dimensionUnit(dimensionUnit: DimensionUnit): String {
        return when (dimensionUnit) {
            DimensionUnit.KILOMETER -> app.getString(R.string.kilometer_abbrev)
            DimensionUnit.MILLIMETER_SQUARE -> app.getString(R.string.millimeter_square_abbrev)
        }
    }

}