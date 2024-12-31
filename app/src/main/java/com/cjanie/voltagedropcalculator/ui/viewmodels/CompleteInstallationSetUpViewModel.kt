package com.cjanie.voltagedropcalculator.ui.viewmodels

import android.app.Application
import androidx.compose.ui.graphics.Color
import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.models.CompleteInstallation
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Copper
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Motor
import com.cjanie.voltagedropcalculator.businesslogic.usecases.InstallationSetUpUseCase
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.ui.theme.placeHolderColor

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
    override var usage: Usage = Usage.LIGHTING

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
            voltage = voltage
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


    private var installation: CompleteInstallation? = null

    fun setUp(): CompleteInstallationPresenter {
        val use = when (usage) {
            Usage.LIGHTING -> Lighting(electricitySupply)
            Usage.MOTOR -> Motor(electricitySupply) // TODO MOTOR IMPL
        }
        val installationSetUp = InstallationSetUpUseCase(use = use, voltage = voltage!!)
        installationSetUp.addInput(cable = createCable(cableViewModel = inputCableViewModel))
        if(outputCircuitsViewModel.isFormComplete()) {
            installationSetUp.addOutput(circuits = arrayOf(createCable(cableViewModel = outputCircuitsViewModel)))

        }
        installation = installationSetUp.getInstallation()
        return CompleteInstallationPresenter(installation!!, application)
    }

    override fun isSetUpComplete(): Boolean {
        return inputCableViewModel.isFormComplete() // TODO
    }

    class CompleteInstallationPresenter(
        installation: CompleteInstallation,
        application: Application,
        inputCableColor: Color = Color.Unspecified,
        outputCircuitsColor: Color = Color.Unspecified,
    ): InstallationPresenter()
    {
        override val usageAsString = usageToString(usage = installation.use.usage, application = application)
        override val usage: Usage = installation.use.usage
        override val electricitySupply = electricitySupplyToString(installation.use.electricitySupply, application)
        override val tension = tensionToString(voltage = installation.nominalVoltage, application = application)
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

    override fun voltageDropResult() : VoltageDropResultPresenter {
        if (installation == null) setUp()
        return VoltageDropResultPresenter(installation!!, application)
    }

}