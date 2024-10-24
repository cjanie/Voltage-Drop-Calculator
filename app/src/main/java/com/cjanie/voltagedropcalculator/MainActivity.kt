package com.cjanie.voltagedropcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.CableViewModel
import com.cjanie.voltagedropcalculator.ui.InputCableViewModel
import com.cjanie.voltagedropcalculator.ui.InstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.InstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.OutputCircuitsViewModel
import com.cjanie.voltagedropcalculator.ui.TensionViewModel
import com.cjanie.voltagedropcalculator.ui.composables.Dropdown
import com.cjanie.voltagedropcalculator.ui.composables.Header
import com.cjanie.voltagedropcalculator.ui.composables.InstallationDrawing
import com.cjanie.voltagedropcalculator.ui.composables.Label
import com.cjanie.voltagedropcalculator.ui.composables.LabeledText
import com.cjanie.voltagedropcalculator.ui.composables.NumberInput
import com.cjanie.voltagedropcalculator.ui.composables.SubmitButton
import com.cjanie.voltagedropcalculator.ui.composables.Title
import com.cjanie.voltagedropcalculator.ui.theme.VoltageDropCalculatorTheme
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.onWhiteColor
import com.cjanie.voltagedropcalculator.ui.theme.whiteColor

class MainActivity : ComponentActivity() {
    
    private val installationSetUpViewModel by lazy { InstallationSetUpViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resultPresenter = ResultPresenter(this)
        
        enableEdgeToEdge()
        setContent {
            VoltageDropCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                    ){

                        Header(
                            text = getString(R.string.app_name)
                        )

                        var installation: InstallationSetUpViewModel.InstallationPresenter? by remember {
                            mutableStateOf(null)
                        }
                        var voltageDropResult: InstallationSetUpViewModel.VoltageDropResultPresenter? by remember {
                            mutableStateOf(null)
                        }

                        if (installation == null) {
                            InstallationSetUp(
                                installationSetUpViewModel = installationSetUpViewModel,
                                finish = fun (installationPresenter: InstallationSetUpViewModel.InstallationPresenter) {
                                    installation = installationPresenter
                                }
                            )
                        } else {
                            InstallationDrawing(installationPresenter = installation!!)
                            Button(onClick = {
                                voltageDropResult = installationSetUpViewModel.voltageDropResult()
                            }) {
                                Text("")
                            }
                        }

                        if(voltageDropResult != null) {
                            Result(voltageDropResultPresenter = voltageDropResult!!
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable()
fun InstallationSetUp(
    installationSetUpViewModel: InstallationSetUpViewModel,
    finish: (installationPresenter: InstallationSetUpViewModel.InstallationPresenter) -> Unit
){
    Column(Modifier.fillMaxSize()) {
        Title(name = installationSetUpViewModel.title, textColor = copperColor)

        var step: InstallationSetUpStep? by remember {
            mutableStateOf(installationSetUpViewModel.installationSetUpStart)
        }


        if(step != null) {

            Label(name = installationSetUpViewModel.stepLabel(step!!))

            fun next() {
                step = installationSetUpViewModel.next(step!!)
            }

            when (step!!) {

                InstallationSetUpStep.DEFINE_USAGE -> DefineInstallationUsage(
                    viewModel = installationSetUpViewModel,
                    next = { next() }
                )
                InstallationSetUpStep.ADD_INPUT_CABLE -> AddInputCable(
                    viewModel = installationSetUpViewModel.inputCableViewModel,
                    next = { next() }
                )
                InstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> AddOutputCircuits(
                    viewModel = installationSetUpViewModel.outputCircuitsViewModel,
                    next = { next() },
                    skip = { next() }
                )
                InstallationSetUpStep.DEFINE_NOMINAL_TENSION -> DefineNominalTension(
                    viewModel = installationSetUpViewModel,
                    next = {
                        next()
                        finish(installationSetUpViewModel.setUp())
                    }
                )
            }
        }
    }
}

@Composable
fun DefineInstallationUsage(viewModel: InstallationSetUpViewModel, next: () -> Unit) {

    var isStepCompleted by remember {
        mutableStateOf(false)
    }

    var isStepEnabled by remember {
        mutableStateOf(true)
    }

    Column {

        DropdownField(
            label = viewModel.functionalContextLabel,
            options = viewModel.functionnalContextOptions,
            select = fun(itemPosition: Int) {
                viewModel.setFunctionnalContext(itemPosition)
            },
            updateFormState = { isStepCompleted = viewModel.isUsageDefined() },
            enabled = isStepEnabled
        )
        DropdownField(
            label = viewModel.electricitySupplyLabel,
            options = viewModel.electricitySupplyOptions,
            select = fun(index: Int) {
                viewModel.setElectricitySupply(index)
            },
            updateFormState = { isStepCompleted = viewModel.isUsageDefined() },
            enabled = isStepEnabled
        )

        if(isStepCompleted) next()
    }
}


@Composable
fun AddInputCable(viewModel: InputCableViewModel, next: () -> Unit) {
    CableForm(viewModel = viewModel, next = next)
}


@Composable
fun CableForm(viewModel: CableViewModel, next: () -> Unit) {
    var isStepCompleted by remember {
        mutableStateOf(false)
    }
    var fieldsEnabled by remember {
        mutableStateOf(true)
    }

    Column {
        NumberInput(
            name = viewModel.lengthLabel,
            select =  fun(value: Float) {
                viewModel.setLength(inKilometer = value)
                isStepCompleted = viewModel.isFormComplete()
            },
            enabled = fieldsEnabled
        )

        fun updateFormState() {
            isStepCompleted = viewModel.isFormComplete()
        }

        DropdownField(
            label = viewModel.phasingLabel,
            options = viewModel.phasingOptions,
            select =  fun(index: Int) {
                viewModel.setPhasing(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        DropdownField(
            label = viewModel.conductorLabel,
            options = viewModel.conductorOptions,
            select =  fun(index: Int) {
                viewModel.setConductor(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        DropdownField(
            label = viewModel.sectionLabel,
            options = viewModel.sectionOptions,
            select =  fun(index: Int) {
                viewModel.setSection(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        DropdownField(
            label = viewModel.intensityLabel,
            options = viewModel.intensityOptions,
            select =  fun(index: Int) {
                viewModel.setIntensity(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        if (isStepCompleted) { next() }
    }

}

@Composable
fun AddOutputCircuits(viewModel: OutputCircuitsViewModel, next: () -> Unit, skip: () -> Unit) {
    CableForm(viewModel = viewModel, next = next)
    Button(onClick = { skip() }) {
        Text(text = viewModel.skipLabel)
    }
}

@Composable
fun DefineNominalTension(viewModel: TensionViewModel, next: () -> Unit) {
    var isStepCompleted by remember {
        mutableStateOf(false)
    }
    var fieldsEnabled by remember {
        mutableStateOf(true)
    }
    fun updateFormState() {
        isStepCompleted = viewModel.isTensionDefined() != null
    }
    Column {
        DropdownField(
            label = viewModel.tensionLabel,
            options = viewModel.tensionOptions,
            select =  fun(index: Int) {
                viewModel.setTension(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )
    }

    if (isStepCompleted) next()


}

@Composable
fun Form(
    formPresenter: FormPresenter,
    fieldsEnabled: Boolean,
    setResult: (voltageDropResult: FormModel.VoltageDropResult) -> Unit
) {

    Column() {
        var isFormComplete by remember {
            mutableStateOf(false)
        }

        NumberInput(
            name = formPresenter.lengthLabel,
            select =  fun(value: Float) {
                formPresenter.setLength(inKilometer = value)
                isFormComplete = formPresenter.isFormComplete()
            },
            enabled = fieldsEnabled
        )

        fun updateFormState() {
            isFormComplete = formPresenter.isFormComplete()
        }

        DropdownField(
            label = formPresenter.functionalContextLabel,
            options = formPresenter.functionnalContexts,
            select = fun(itemPosition: Int) {
                formPresenter.setFunctionnalContext(itemPosition)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        DropdownField(
            label = formPresenter.electricitySupplyLabel,
            options = formPresenter.electricitySupplyOptions,
            select = fun(index: Int) {
                formPresenter.setElectricitySupply(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        DropdownField(
            label = formPresenter.phasingLabel,
            options = formPresenter.phasingOptions,
            select =  fun(index: Int) {
                formPresenter.setPhasing(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        DropdownField(
            label = formPresenter.conductorLabel,
            options = formPresenter.conductorOptions,
            select =  fun(index: Int) {
                formPresenter.setConductor(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        DropdownField(
            label = formPresenter.sectionLabel,
            options = formPresenter.sectionOptions,
            select =  fun(index: Int) {
                formPresenter.setSection(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        DropdownField(
            label = formPresenter.intensityLabel,
            options = formPresenter.intensityOptions,
            select =  fun(index: Int) {
                formPresenter.setIntensity(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        DropdownField(
            label = formPresenter.tensionLabel,
            options = formPresenter.tensionOptions,
            select =  fun(index: Int) {
                formPresenter.setTension(index)
            },
            updateFormState = { updateFormState() },
            enabled = fieldsEnabled
        )

        if(fieldsEnabled) {
            SubmitButton(
                text = formPresenter.calculateVoltageDropLabel,
                onClick = {
                    try {
                        setResult(formPresenter.calculateVoltageDrop())
                    } catch (e: NullValueException) {
                        e.printStackTrace()
                    }
                },
                enabled = isFormComplete,
            )
        }

    }
}

@Composable
fun DropdownField(
    label: String,
    options: Array<String>,
    select: (itemPosition: Int) -> Unit,
    updateFormState: () -> Unit,
    enabled: Boolean
) {
    Dropdown(
        name = label,
        options = options,
        select =  fun(itemPosition: Int) {
            select(itemPosition)
            updateFormState()
        },
        enabled = enabled
    )
}

@Composable
fun Result(voltageDropResultPresenter: InstallationSetUpViewModel.VoltageDropResultPresenter) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(voltageDropResultPresenter.warningColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(whiteColor),
            horizontalAlignment = Alignment.CenterHorizontally
        )  {
            LabeledText(
                label = voltageDropResultPresenter.inVoltLabel,
                text = voltageDropResultPresenter.inVoltValue,
                textColor = onWhiteColor
            )
            LabeledText(
                label = voltageDropResultPresenter.asPercentageLabel,
                text = voltageDropResultPresenter.asPercentageValue,
                textColor = onWhiteColor
            )
        }

        Title(
            name = voltageDropResultPresenter.isVoltageDropAcceptableWarningText,
            textColor = voltageDropResultPresenter.onWarningColor
        )

        LabeledText(
            label = voltageDropResultPresenter.maxVoltageDropLimitPercentageLabel,
            text = voltageDropResultPresenter.maxVoltageDropLimitPercentageValue,
            textColor = voltageDropResultPresenter.onWarningColor
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VoltageDropCalculatorTheme {

    }
}