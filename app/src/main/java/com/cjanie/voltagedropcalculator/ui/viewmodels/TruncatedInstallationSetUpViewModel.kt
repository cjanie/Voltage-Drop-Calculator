package com.cjanie.voltagedropcalculator.ui.viewmodels

import android.app.Application
import androidx.compose.ui.graphics.Color
import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.models.TruncatedInstallation
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Copper
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Motor
import com.cjanie.voltagedropcalculator.businesslogic.usecases.TruncatedInstallationSetUpUseCase
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop
import com.cjanie.voltagedropcalculator.ui.theme.placeHolderColor
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpViewModel.CablePresenter

enum class TruncatedInstallationSetUpStep {
    DEFINE_ELECTRICITY_SUPPLY,
    DEFINE_NOMINAL_TENSION,
    DEFINE_INPUT_CABLE_VOLTAGE_DROP,
    ADD_OUTPUT_CIRCUITS,
    DEFINE_USAGE
}

class TruncatedInstallationSetUpViewModel(private val application: Application) :
    InstallationSetUpViewModel(application) {

        override var usage: Usage = Usage.MOTOR

    var inputCableVoltageDrop = VoltageDrop(10f)
    val inputCableVoltageDropLabel = application.getString(R.string.input_cable_voltage_drop_in_volt_label)

    fun setInputCableVoltageDrop(inVolt: Float) {
        inputCableVoltageDrop = VoltageDrop(inVolt)
    }


    companion object {
        fun installationSetUpStepLabel(step: TruncatedInstallationSetUpStep, application: Application): String {
            return when (step) {
                TruncatedInstallationSetUpStep.DEFINE_USAGE -> application.getString(R.string.define_installation_usage_label)
                TruncatedInstallationSetUpStep.DEFINE_ELECTRICITY_SUPPLY -> application.getString(R.string.define_electricity_supply)
                TruncatedInstallationSetUpStep.DEFINE_NOMINAL_TENSION -> application.getString(R.string.define_nominal_tension)
                TruncatedInstallationSetUpStep.DEFINE_INPUT_CABLE_VOLTAGE_DROP -> application.getString(R.string.define_input_cable_voltage_drop)
                TruncatedInstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> application.getString(R.string.add_output_circuits_label)
                TruncatedInstallationSetUpStep.DEFINE_USAGE -> application.getString(R.string.define_installation_usage_label)

            }
        }
    }


    fun installationSetUpStepLabel(step: TruncatedInstallationSetUpStep): String {
        return installationSetUpStepLabel(step, application)
    }

    fun updateInstallationPlaceHolder(): TruncatedInstallationPresenter {
        val installationSetUpUseCase = TruncatedInstallationSetUpUseCase(
            usage = usage,
            electricitySupply = electricitySupply,
            nominalTension = tension,
            inputCableVoltageDrop = inputCableVoltageDrop
        )
        val use = when (usage) {
            Usage.MOTOR -> Motor(electricitySupply)
            Usage.LIGHTING -> Lighting(electricitySupply)
        }

        val output = try {
            createCable(cableViewModel = outputCircuitsViewModel)
        } catch (e: NullValueException) {
            LineThreePhase(
                phaseShift = use.phaseShift,
                conductor = Copper(),
                section = Section(35f),
                intensity = Intensity(100f),
                length = Length(0.05f)
            )
        }
        installationSetUpUseCase.addOutputCircuit(output)

        val outputCircuitsColor = if (!outputCircuitsViewModel.isFormComplete()) placeHolderColor else Color.Unspecified

        return TruncatedInstallationPresenter(
            installation = installationSetUpUseCase.getInstallation(),
            application = application,
            outputCircuitsColor = outputCircuitsColor
        )
    }

    class TruncatedInstallationPresenter(
        installation: TruncatedInstallation,
        application: Application,
        outputCircuitsColor: Color = Color.Unspecified
    ) : InstallationPresenter() {

        override val usageAsString = usageToString(usage = installation.usage, application = application)
        override val usage: Usage = installation.usage
        override val electricitySupply = electricitySupplyToString(installation.electricitySupply, application)
        override val tension = tensionToString(tension = installation.nominalTension, application = application)
        val inputCableVoltageDrop ="- ${tensionToString(tension = Tension(installation.inputCableVoltageDrop.inVolt), application = application)}"

        val outputCircuitsPresenter =
            if (!installation.outputCircuits.isEmpty())
                CablePresenter(
                    cable = installation.outputCircuits[0],
                    application = application,
                    textColor = outputCircuitsColor
                )
            else null
    }

}