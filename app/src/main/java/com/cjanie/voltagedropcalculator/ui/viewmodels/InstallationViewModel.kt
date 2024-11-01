package com.cjanie.voltagedropcalculator.ui.viewmodels

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.PhaseShiftInconsistancyException
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.factories.LineFactory
import com.cjanie.voltagedropcalculator.businesslogic.models.CalculateVoltageDrop
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Copper
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Motor
import com.cjanie.voltagedropcalculator.businesslogic.usecases.InstallationSetUpUseCase
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.onGreenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.onRedWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.placeHolderColor
import com.cjanie.voltagedropcalculator.ui.theme.redWarningColor

enum class InstallationSetUpStep {
    DEFINE_USAGE,
    DEFINE_ELECTRICITY_SUPPLY,
    DEFINE_NOMINAL_TENSION,
    ADD_INPUT_CABLE,
    ADD_OUTPUT_CIRCUITS,

}

class InstallationViewModel(
    private val application: Application
) : AndroidViewModel(application),
    UsageViewModel,
    ElectricitySupplyViewModel,
    TensionViewModel
{
    // Title
    val title = application.getString(R.string.installation_setup_title)

    companion object {

        private fun usageToString(usage: FunctionalContext, application: Application): String {
            return when (usage) {
                FunctionalContext.LIGHTING -> application.getString(R.string.functional_context_lighting)
                FunctionalContext.MOTOR -> application.getString(R.string.functional_context_motor)
            }
        }

        private fun electricitySupplyToString(electricitySupply: ElectricitySupply, application: Application): String {
            return when (electricitySupply) {
                ElectricitySupply.PRIVATE -> application.getString(R.string.electricity_supply_private)
                ElectricitySupply.PUBLIC -> application.getString(R.string.electricity_supply_public)
            }
        }

        private fun tensionToString(tension: Tension, application: Application): String {
            return "${tension.inVolt.toInt()} ${application.getString(R.string.tension_unit)}"
        }

    }

    fun stepLabel(step: InstallationSetUpStep): String {
        return when (step) {
            InstallationSetUpStep.DEFINE_USAGE -> application.getString(R.string.define_installation_usage_label)
            InstallationSetUpStep.ADD_INPUT_CABLE -> application.getString(R.string.add_input_cable_label)
            InstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> application.getString(R.string.add_output_circuits_label)
            InstallationSetUpStep.DEFINE_NOMINAL_TENSION -> application.getString(R.string.define_nominal_tension)
            InstallationSetUpStep.DEFINE_ELECTRICITY_SUPPLY -> application.getString(R.string.define_electricity_supply)
        }
    }

    // UsageViewModel Impl

    private var usage: FunctionalContext = FunctionalContext.LIGHTING
    private val usageValues: Set<FunctionalContext> = FunctionalContext.values().toHashSet()
    override val usageLabel = application.getString(R.string.usage_label)
    override val usageOptions: Array<String> = usageValues
        .map { usageToString(it, application) }.toTypedArray()
    override fun setUsage(itemPosition: Int) {
        usage = usageValues.toList()[itemPosition]
    }

    // ElectricitySupplyViewModel Impl
    private var electricitySupply: ElectricitySupply = ElectricitySupply.PUBLIC
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
    private val tensionValues: Set<Tension> = setOf(
        Tension(inVolt = 3f),
        Tension(24f),
        Tension(230f),
    )
    private var tension: Tension = tensionValues.toList()[2]

    override val tensionLabel = application.getString(R.string.tension_label)
    override val tensionOptions: Array<String> = tensionValues
        .map { tensionToString(it, application) }.toTypedArray()
    override fun setTension(itemPosition: Int) {
        tension = tensionValues.toList()[itemPosition]
    }

    override fun isTensionDefined(): Boolean {
        return tension != null
    }

    // ViewModels for cables


    val inputCableViewModel = InputCableViewModel(application)

    val outputCircuitsViewModel = OutputCircuitsViewModel(application)

    // PlaceHolder

    fun updateInstallationPlaceHolder(): InstallationPresenter {

        var installationSetUpUseCase = InstallationSetUpUseCase(

            use = when(usage) {
                FunctionalContext.LIGHTING -> Lighting(electricitySupply)
                FunctionalContext.MOTOR -> Motor(electricitySupply)
                            },
            tension = tension
        )

        val inputLine = try {
            createCable(cableViewModel = inputCableViewModel)

        } catch (e: NullValueException) {
            LineSinglePhase(installationSetUpUseCase.use.phaseShift, Copper(), Section(1f), Intensity(2f), Length(1f))
        }

            installationSetUpUseCase.addInput(cable = inputLine)



        val output = try {
            createCable(cableViewModel = outputCircuitsViewModel)
        } catch (e: NullValueException) {
            LineSinglePhase(installationSetUpUseCase.use.phaseShift, Copper(), Section(50f), Intensity(2f), Length(0.02f))

        }
        installationSetUpUseCase.addOutput(arrayOf(output))

        val inputCableColor = if (!inputCableViewModel.isFormComplete()) placeHolderColor else Color.Unspecified
        val outputCircuitsColor = if (!outputCircuitsViewModel.isFormComplete()) placeHolderColor else Color.Unspecified
        return InstallationPresenter(
            installation = installationSetUpUseCase.getInstallation()!!,
            application,
            inputCableColor = inputCableColor,
            outputCircuitsColor = outputCircuitsColor)
    }


    private var installation: Installation? = null

    fun setUp(): InstallationPresenter {
        val use = when (usage) {
            FunctionalContext.LIGHTING -> Lighting(electricitySupply)
            FunctionalContext.MOTOR -> Motor(electricitySupply) // TODO MOTOR IMPL
        }
        val installationSetUp = InstallationSetUpUseCase(use = use, tension = tension!!)
        installationSetUp.addInput(cable = createCable(cableViewModel = inputCableViewModel))
        if(outputCircuitsViewModel.isFormComplete()) {
            installationSetUp.addOutput(circuits = arrayOf(createCable(cableViewModel = outputCircuitsViewModel)))

        }
        installation = installationSetUp.getInstallation()
        return InstallationPresenter(installation!!, application)
    }

    fun isSetUpComplete(): Boolean {
        return inputCableViewModel.isFormComplete() // TODO
    }

    private fun createCable(cableViewModel: CableViewModel): Line {
        if(!cableViewModel.isFormComplete())
            throw NullValueException()
        return LineFactory.line(
            functionalContext = usage,
            electricitySupply = electricitySupply,
            phasing = cableViewModel.phasing!!,
            conductorMaterial = cableViewModel.conductor!!,
            section = cableViewModel.section!!,
            intensity = cableViewModel.intensity!!,
            length = cableViewModel.length!!
        )
    }

    interface UsagePresenter {
        val usage: String
    }

    interface ElectricitySupplyPresenter {
        val electricitySupply: String
    }

    interface TensionPresenter {
        val tension: String
    }
    class InstallationPresenter(
        installation: Installation,
        application: Application,
        inputCableColor: Color = Color.Unspecified,
        outputCircuitsColor: Color = Color.Unspecified,
    ): UsagePresenter, ElectricitySupplyPresenter, TensionPresenter

    {
        override val usage = usageToString(usage = installation.use.usage, application = application)
        override val electricitySupply = electricitySupplyToString(installation.use.electricitySupply, application)
        override val tension = tensionToString(tension = installation.nominalTension, application = application)
        val inputCablePresenter = CablePresenter(
            cable = installation.input,
            application = application,
            textColor = inputCableColor
        )
        val outputCircuitsPresenter =
            if (!installation.output.isEmpty())
                CablePresenter(
                    cable = installation.output[0],
                    application = application,
                    textColor = outputCircuitsColor
                )
            else null
    }

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

    val calculateVoltageDropLabel = application.getString(R.string.calculate_voltage_drop_label)

    class VoltageDropResultPresenter(calculateVoltageDrop: CalculateVoltageDrop, application: Application) {
        val inVoltLabel = application.getString(R.string.voltage_drop_in_volt_label)
        val inVoltValue = tensionToString(Tension(calculateVoltageDrop.voltageDropInVolt), application )
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

    fun voltageDropResult() : VoltageDropResultPresenter {
        if (installation == null) setUp()
        return VoltageDropResultPresenter(installation!!, application)
    }

}