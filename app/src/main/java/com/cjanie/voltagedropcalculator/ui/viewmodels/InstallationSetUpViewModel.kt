package com.cjanie.voltagedropcalculator.ui.viewmodels

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.factories.LineFactory
import com.cjanie.voltagedropcalculator.businesslogic.models.CalculateVoltageDrop
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.onGreenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.onRedWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.redWarningColor

abstract class InstallationSetUpViewModel(application: Application) : AndroidViewModel(application),
    UsageViewModel,
    ElectricitySupplyViewModel,
    TensionViewModel {

    companion object {

        fun usageToString(usage: Usage, application: Application): String {
            return when (usage) {
                Usage.LIGHTING -> application.getString(R.string.functional_context_lighting)
                Usage.MOTOR -> application.getString(R.string.functional_context_motor)
            }
        }

        fun electricitySupplyToString(electricitySupply: ElectricitySupply, application: Application): String {
            return when (electricitySupply) {
                ElectricitySupply.PRIVATE -> application.getString(R.string.electricity_supply_private)
                ElectricitySupply.PUBLIC -> application.getString(R.string.electricity_supply_public)
            }
        }

        fun tensionToString(voltage: Voltage, application: Application): String {
            return "${voltage.inVolt.toInt()} ${application.getString(R.string.tension_unit)}"
        }

    }

    // UsageViewModel Impl

    protected abstract var usage: Usage
    private val usageValues: Set<Usage> = Usage.values().toHashSet()
    override val usageLabel = application.getString(R.string.usage_label)
    override val usageOptions: Array<String> = usageValues
        .map { usageToString(it, application) }.toTypedArray()
    override fun setUsage(itemPosition: Int) {
        usage = usageValues.toList()[itemPosition]
    }

    // ElectricitySupplyViewModel Impl
    protected var electricitySupply: ElectricitySupply = ElectricitySupply.PUBLIC
    private val electricitySupplyValues: Set<ElectricitySupply> = ElectricitySupply.values().toHashSet()
    override val electricitySupplyLabel = application.getString(R.string.electricity_supply_label)
    override val electricitySupplyOptions: Array<String> = electricitySupplyValues
        .map { electricitySupply ->
            when (electricitySupply) {
                ElectricitySupply.PUBLIC -> application.getString(R.string.electricity_supply_public)
                ElectricitySupply.PRIVATE -> application.getString(R.string.electricity_supply_private)
            }
        }.toTypedArray()

    override fun setElectricitySupply(itemPosition: Int) {
        electricitySupply = electricitySupplyValues.toList()[itemPosition]
    }

    override fun isElectricitySupplyDefined(): Boolean {
        return electricitySupply != null
    }

    // TensionViewModel Impl
    private val voltageValues: Set<Voltage> = setOf(
        Voltage(inVolt = 3f),
        Voltage(24f),
        Voltage(230f),
    )
    protected var voltage: Voltage = voltageValues.toList()[2]

    override val tensionLabel = application.getString(R.string.tension_label)
    override val tensionOptions: Array<String> = voltageValues
        .map { tensionToString(it, application) }.toTypedArray()
    override fun setTension(itemPosition: Int) {
        voltage = voltageValues.toList()[itemPosition]
    }

    override fun isTensionDefined(): Boolean {
        return voltage != null
    }

    val outputCircuitsViewModel = OutputCircuitsViewModel(application)

    protected fun createCable(cableViewModel: CableViewModel): Line {
        if(!cableViewModel.isFormComplete())
            throw NullValueException()
        return LineFactory.line(
            usage = usage,
            electricitySupply = electricitySupply,
            phasing = cableViewModel.phasing!!,
            conductorMaterial = cableViewModel.conductor!!,
            section = cableViewModel.section!!,
            intensity = cableViewModel.intensity!!,
            length = cableViewModel.length!!
        )
    }

    abstract fun isSetUpComplete(): Boolean

    val calculateVoltageDropLabel = application.getString(R.string.calculate_voltage_drop_label)

    abstract fun voltageDropResult(): VoltageDropResultPresenter

    // Installation Presenter interfaces
    interface UsagePresenter {
        val usageAsString: String
        val usage: Usage

    }

    interface ElectricitySupplyPresenter {
        val electricitySupply: String
    }

    interface TensionPresenter {
        val tension: String
    }

    abstract class InstallationPresenter : UsagePresenter, ElectricitySupplyPresenter, TensionPresenter

    class CablePresenter(
        cable: Line, application: Application,
        val textColor: Color = Color.Unspecified
    ) {
        val phasing = cable.phasing
        private val conductor =
            CableViewModel.conductorToString(cable.conductor.material, application)
        private val section = CableViewModel.sectionToString(cable.section, application)
        private val intensity = CableViewModel.intensityToString(cable.intensity, application)
        private val length = CableViewModel.lengthToString(cable.length, application)
        val cableText = "$conductor\n$section\n$intensity\n${length}"
    }

    class VoltageDropResultPresenter(
        calculateVoltageDrop: CalculateVoltageDrop,
        application: Application) {
        val inVoltLabel = application.getString(R.string.voltage_drop_in_volt_label)
        val inVoltValue = tensionToString(Voltage(calculateVoltageDrop.voltageDropInVolt), application )
        val asPercentageLabel = application.getString(R.string.voltage_drop_percentage_label)
        val asPercentageValue = "${calculateVoltageDrop.voltageDropPercentage.toInt()} ${application.getString(R.string.percentage_sign)}"
        val isVoltageDropAcceptableWarningText = if (calculateVoltageDrop.isVoltageDropAcceptable)
            "${application.getString(R.string.voltage_drop_acceptable_result)}"
        else "${application.getString(R.string.voltage_drop_not_acceptable_result)}"
        val maxVoltageDropLimitPercentageLabel = application.getString(R.string.max_voltage_drop_acceptable_percentage_label)
        val maxVoltageDropLimitPercentageValue = "${calculateVoltageDrop.maxVoltageDropLimitPercentage.toInt()} ${application.getString(R.string.percentage_sign)}"
        val warningColor = if (calculateVoltageDrop.isVoltageDropAcceptable) greenWarningColor
        else redWarningColor
        val onWarningColor = if (calculateVoltageDrop.isVoltageDropAcceptable)
            onGreenWarningColor
        else onRedWarningColor
    }
    }