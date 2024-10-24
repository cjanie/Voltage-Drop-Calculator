package com.cjanie.voltagedropcalculator.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionnalContext
import com.cjanie.voltagedropcalculator.businesslogic.factories.LineFactory
import com.cjanie.voltagedropcalculator.businesslogic.models.CalculateVoltageDrop
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.usecases.InstallationSetUpUseCase
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.onGreenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.onRedWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.redWarningColor

enum class InstallationSetUpStep {
    DEFINE_USAGE,
    ADD_INPUT_CABLE,
    ADD_OUTPUT_CIRCUITS,
    DEFINE_NOMINAL_TENSION
}

class InstallationSetUpViewModel(
    private val application: Application
) : AndroidViewModel(application),
    UsageViewModel,
    TensionViewModel
{
    // Title
    val title = application.getString(R.string.installation_setup_title)

    val installationSetUpStart: InstallationSetUpStep? = InstallationSetUpStep.DEFINE_USAGE

    companion object {
        private fun functionnalContextToString(functionalContext: FunctionnalContext, application: Application): String {
            return when (functionalContext) {
                FunctionnalContext.LIGHTING -> application.getString(R.string.functional_context_lighting)
                FunctionnalContext.MOTOR -> application.getString(R.string.functional_context_motor)
            }
        }

        private fun electricitySupplyToString(electricitySupply: ElectricitySupply, application: Application): String {
            return when (electricitySupply) {
                ElectricitySupply.PRIVATE -> application.getString(R.string.electricity_supply_private)
                ElectricitySupply.PUBLIC -> application.getString(R.string.electricity_supply_public)
            }
        }

        private fun tensionToString(tension: Tension, application: Application): String {
            return "${
                if (tension.inVolt.toString().contains(".0"))
                    tension.inVolt.toInt() else tension.inVolt
            } ${application.getString(R.string.tension_unit)}"
        }
    }

    fun next(currentStep: InstallationSetUpStep): InstallationSetUpStep? {
        return when (currentStep) {
            InstallationSetUpStep.DEFINE_USAGE -> InstallationSetUpStep.ADD_INPUT_CABLE
            InstallationSetUpStep.ADD_INPUT_CABLE -> InstallationSetUpStep.ADD_OUTPUT_CIRCUITS
            InstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> InstallationSetUpStep.DEFINE_NOMINAL_TENSION
            InstallationSetUpStep.DEFINE_NOMINAL_TENSION -> null
        }
    }

    fun stepLabel(step: InstallationSetUpStep): String {
        return when (step) {
            InstallationSetUpStep.DEFINE_USAGE -> application.getString(R.string.define_installation_usage_label)
            InstallationSetUpStep.ADD_INPUT_CABLE -> application.getString(R.string.add_input_cable_label)
            InstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> application.getString(R.string.add_output_circuits_label)
            InstallationSetUpStep.DEFINE_NOMINAL_TENSION -> application.getString(R.string.define_nominal_tension)
        }
    }

    // UsageViewModel Impl

    private var functionnalContext: FunctionnalContext? = null
    private val functionnalContextValues: Set<FunctionnalContext> = FunctionnalContext.values().toHashSet()

    override val functionalContextLabel = application.getString(R.string.functional_context_label)
    override val functionnalContextOptions: Array<String> = functionnalContextValues
        .map { functionnalContextToString(it, application) }.toTypedArray()
    override fun setFunctionnalContext(itemPosition: Int) {
        functionnalContext = functionnalContextValues.toList()[itemPosition]
    }

    private var electricitySupply: ElectricitySupply? = null
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

    override fun isUsageDefined(): Boolean {
        return functionnalContext != null && electricitySupply != null
    }


    val inputCableViewModel = InputCableViewModel(application)

    val outputCircuitsViewModel = OutputCircuitsViewModel(application)

    // TensionViewModel Impl
    private var tension: Tension? = null
    private val tensionValues: Set<Tension> = setOf(
        Tension(inVolt = 3f),
        Tension(24f),
        Tension(230f),
    )
    override val tensionLabel = application.getString(R.string.tension_label)
    override val tensionOptions: Array<String> = tensionValues
        .map { tensionToString(it, application) }.toTypedArray()
    override fun setTension(itemPosition: Int) {
        tension = tensionValues.toList()[itemPosition]
    }

    override fun isTensionDefined(): Boolean {
        return tension != null
    }

    private var installation: Installation? = null

    fun setUp(): InstallationPresenter {
        if (functionnalContext == null || electricitySupply == null || tension == null)
            throw NullValueException()
        val use = when (functionnalContext!!) {
            FunctionnalContext.LIGHTING -> Lighting(electricitySupply!!)
            FunctionnalContext.MOTOR -> Lighting(electricitySupply!!) // TODO MOTOR IMPL
        }
        val installationSetUp = InstallationSetUpUseCase(use = use, tension = tension!!)
        installationSetUp.addInput(cable = createCable(cableViewModel = inputCableViewModel))
        if(outputCircuitsViewModel.isFormComplete()) {
            installationSetUp.addOutput(circuits = arrayOf(createCable(cableViewModel = outputCircuitsViewModel)))

        }
        installation = installationSetUp.getInstallation()
        return InstallationPresenter(installation!!, application)
    }

    private fun createCable(cableViewModel: CableViewModel): Line {
        if(functionnalContext == null || electricitySupply == null || (cableViewModel is InputCableViewModel && !cableViewModel.isFormComplete()))
            throw NullValueException()
        return LineFactory.line(
            functionnalContext = functionnalContext!!,
            electricitySupply = electricitySupply!!,
            phasing = cableViewModel.phasing!!,
            conductorMaterial = cableViewModel.conductor!!,
            section = cableViewModel.section!!,
            intensity = cableViewModel.intensity!!,
            length = cableViewModel.length!!
        )
    }

    class InstallationPresenter(installation: Installation, application: Application) {
        val functionalContext = functionnalContextToString(functionalContext = installation.use.functionalContext, application = application)
        val electricitySupply = electricitySupplyToString(installation.use.electricitySupply, application)
        val tension = tensionToString(tension = installation.nominalTension, application = application)
        val inputCablePresenter = CablePresenter(cable = installation.input, application = application)
        val outputCircuitsPresenter = if (!installation.output.isEmpty())
            CablePresenter(cable = installation.output[0], application)
        else null
    }
    class CablePresenter(cable: Line, application: Application) {
        private val phasing = CableViewModel.phasingToString(cable.phasing, application)
        private val conductor = CableViewModel.conductorToString(cable.conductor.material, application)
        private val section = CableViewModel.sectionToString(cable.section, application)
        private val intensity = CableViewModel.intensityToString(cable.intensity, application)
        private val length = CableViewModel.lengthToString(cable.length, application)
        val cableText = "$phasing\n$conductor\n$section\n$intensity\n${length}"

    }

    class VoltageDropResultPresenter(calculateVoltageDrop: CalculateVoltageDrop, application: Application) {
        val inVoltLabel = application.getString(R.string.voltage_drop_in_volt_label)
        val inVoltValue = tensionToString(Tension(calculateVoltageDrop.voltageDropInVolt), application )
        val asPercentageLabel = application.getString(R.string.voltage_drop_percentage_label)
        val asPercentageValue = "${calculateVoltageDrop.voltageDropPercentage} ${application.getString(R.string.percentage_sign)}"
        val isVoltageDropAcceptableWarningText = if (calculateVoltageDrop.isVoltageDropAcceptable)
            "${application.getString(R.string.voltage_drop_acceptable_result)}"
        else "${application.getString(R.string.voltage_drop_not_acceptable_result)}"
        val maxVoltageDropLimitPercentageLabel = application.getString(R.string.max_voltage_drop_acceptable_percentage_label)
        val maxVoltageDropLimitPercentageValue = "${calculateVoltageDrop.maxVoltageDropLimitPercentage} ${application.getString(R.string.percentage_sign)}"
        val warningColor = if (calculateVoltageDrop.isVoltageDropAcceptable) greenWarningColor
            else redWarningColor
        val onWarningColor = if (calculateVoltageDrop.isVoltageDropAcceptable)
            onGreenWarningColor
            else onRedWarningColor
    }

    fun voltageDropResult() : VoltageDropResultPresenter {
        if (installation == null) setUp()
        return VoltageDropResultPresenter(installation!!, application)
    }

}