package com.cjanie.voltagedropcalculator.ui.viewmodels

import android.app.Application
import androidx.compose.ui.graphics.Color
import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
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

enum class CompleteInstallationSetUpStep {
    DEFINE_USAGE,
    DEFINE_ELECTRICITY_SUPPLY,
    DEFINE_NOMINAL_TENSION,
    ADD_INPUT_CABLE,
    ADD_OUTPUT_CIRCUITS,

}

class CompleteInstallationSetUpViewModel(
    private val application: Application
) : InstallationSetUpViewModel(application)
{
    companion object {
        fun installationSetUpStepLabel(step: CompleteInstallationSetUpStep, application: Application): String {
            return when (step) {
                CompleteInstallationSetUpStep.DEFINE_USAGE -> application.getString(R.string.define_installation_usage_label)
                CompleteInstallationSetUpStep.ADD_INPUT_CABLE -> application.getString(R.string.add_input_cable_label)
                CompleteInstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> application.getString(R.string.add_output_circuits_label)
                CompleteInstallationSetUpStep.DEFINE_NOMINAL_TENSION -> application.getString(R.string.define_nominal_tension)
                CompleteInstallationSetUpStep.DEFINE_ELECTRICITY_SUPPLY -> application.getString(R.string.define_electricity_supply)
            }
        }
    }


    fun installationSetUpStepLabel(step: CompleteInstallationSetUpStep): String {
        return installationSetUpStepLabel(step, application)
    }

    // ViewModels for cables

    val inputCableViewModel = InputCableViewModel(application)

    // the outputCableViewModel initialized in parent class

    // PlaceHolder

    fun updateInstallationPlaceHolder(): CompleteInstallationPresenter {

        var installationSetUpUseCase = InstallationSetUpUseCase(

            use = when(usage) {
                Usage.LIGHTING -> Lighting(electricitySupply)
                Usage.MOTOR -> Motor(electricitySupply)
                            },
            tension = tension
        )

        val inputLine = try {
            createCable(cableViewModel = inputCableViewModel)

        } catch (e: NullValueException) {
            LineThreePhase(installationSetUpUseCase.use.phaseShift, Copper(), Section(1f), Intensity(2f), Length(1f))
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
        return CompleteInstallationPresenter(
            installation = installationSetUpUseCase.getInstallation()!!,
            application,
            inputCableColor = inputCableColor,
            outputCircuitsColor = outputCircuitsColor)
    }


    private var installation: Installation? = null

    fun setUp(): CompleteInstallationPresenter {
        val use = when (usage) {
            Usage.LIGHTING -> Lighting(electricitySupply)
            Usage.MOTOR -> Motor(electricitySupply) // TODO MOTOR IMPL
        }
        val installationSetUp = InstallationSetUpUseCase(use = use, tension = tension!!)
        installationSetUp.addInput(cable = createCable(cableViewModel = inputCableViewModel))
        if(outputCircuitsViewModel.isFormComplete()) {
            installationSetUp.addOutput(circuits = arrayOf(createCable(cableViewModel = outputCircuitsViewModel)))

        }
        installation = installationSetUp.getInstallation()
        return CompleteInstallationPresenter(installation!!, application)
    }

    fun isSetUpComplete(): Boolean {
        return inputCableViewModel.isFormComplete() // TODO
    }

    class CompleteInstallationPresenter(
        installation: Installation,
        application: Application,
        inputCableColor: Color = Color.Unspecified,
        outputCircuitsColor: Color = Color.Unspecified,
    ): InstallationPresenter()
    {
        override val usageAsString = usageToString(usage = installation.use.usage, application = application)
        override val usage: Usage = installation.use.usage
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