package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cjanie.voltagedropcalculator.ui.composables.commons.Dropdown
import com.cjanie.voltagedropcalculator.ui.composables.commons.NumberInput
import com.cjanie.voltagedropcalculator.ui.composables.commons.Subtitle
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.viewmodels.CableViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.ElectricitySupplyViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.InputCableViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.OutputCircuitsViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.TensionViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.TruncatedInstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.TruncatedInstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.UsageViewModel

@Composable
fun TruncatedInstallationSetUpEdition(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: TruncatedInstallationSetUpViewModel,
    step: TruncatedInstallationSetUpStep,
    next: () -> Unit
) {
    when(step!!) {
        TruncatedInstallationSetUpStep.DEFINE_ELECTRICITY_SUPPLY -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top
        ) {
            EditElectricitySupply(
                viewModel = viewModel,
                next = { next() }
            )
        }
        TruncatedInstallationSetUpStep.DEFINE_NOMINAL_TENSION -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top
        ){
            EditNominalTension(
                viewModel = viewModel,
                next = { next() }
            )
        }
        TruncatedInstallationSetUpStep.DEFINE_INPUT_CABLE_VOLTAGE_DROP -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top
        ){
            EditInputCableVoltageDrop(
                viewModel = viewModel,
                next = { next() }
            )
        }
        TruncatedInstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center
        ){
            AddOutputCircuits(
                viewModel = viewModel.outputCircuitsViewModel,
                next = { next() },
                skip = { next() }
            )
        }
        TruncatedInstallationSetUpStep.DEFINE_USAGE -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Bottom
        ){
            EditUsage(
                viewModel = viewModel,
                next = { next() }
            )
        }
    }
}

@Composable()
fun CompleteInstallationSetUpEdition(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: CompleteInstallationSetUpViewModel,
    step: CompleteInstallationSetUpStep,
    next: () -> Unit
){
            when (step) {

                CompleteInstallationSetUpStep.DEFINE_USAGE -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Bottom
                ){
                    EditUsage(
                        viewModel = viewModel,
                        next = { next() },
                    )
                }
                CompleteInstallationSetUpStep.ADD_INPUT_CABLE -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Center
                ){
                    Subtitle(
                        text = viewModel.installationSetUpStepLabel(step),
                        color = greenWarningColor
                    )
                    AddInputCable(
                        viewModel = viewModel.inputCableViewModel,
                        next = { next() }
                    )
                }
                CompleteInstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Center
                ){
                    Subtitle(
                        text = viewModel.installationSetUpStepLabel(step),
                        color = greenWarningColor
                    )
                    AddOutputCircuits(
                        viewModel = viewModel.outputCircuitsViewModel,
                        next = { next() },
                        skip = { next() }
                    )

                }
                CompleteInstallationSetUpStep.DEFINE_NOMINAL_TENSION -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Top
                ){
                    EditNominalTension(
                        viewModel = viewModel,
                        next = { next() }
                    )
                }

                CompleteInstallationSetUpStep.DEFINE_ELECTRICITY_SUPPLY -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Top
                ) {
                    EditElectricitySupply(
                        viewModel = viewModel,
                        next = { next() }
                    )
                }
            }


}

@Composable
fun EditUsage(
    viewModel: UsageViewModel, next: () -> Unit
) {

    var isStepEnabled by remember {
        mutableStateOf(true)
    }



        Dropdown(
            label = viewModel.usageLabel,
            options = viewModel.usageOptions,
            select = fun(itemPosition: Int) {
                viewModel.setUsage(itemPosition)
                next()
            },
            enabled = isStepEnabled,
            isExpanded = true
        )


}

@Composable
fun EditElectricitySupply(
    viewModel: ElectricitySupplyViewModel, next: () -> Unit
) {

    var isStepCompleted by remember {
        mutableStateOf(false)
    }

    if(isStepCompleted) next()

    fun updateStepState() {
        isStepCompleted = viewModel.isElectricitySupplyDefined()
    }

    var isStepEnabled by remember {
        mutableStateOf(true)
    }

    Dropdown(
        label = viewModel.electricitySupplyLabel,
        options = viewModel.electricitySupplyOptions,
        select = fun(index: Int) {
            viewModel.setElectricitySupply(index)
            updateStepState()
        },
        enabled = isStepEnabled,
        isExpanded = true
    )
}


@Composable
fun AddInputCable(viewModel: InputCableViewModel, next: () -> Unit) {
    CableForm(viewModel = viewModel, next = next)
}


@Composable
fun CableForm(
    viewModel: CableViewModel,
    next: () -> Unit
) {
    var isStepCompleted by remember {
        mutableStateOf(false)
    }
    var fieldsEnabled by remember {
        mutableStateOf(true)
    }

    fun updateStepState() {
        isStepCompleted = viewModel.isFormComplete()
    }

    Column {
        NumberInput(
            name = viewModel.lengthLabel,
            select =  fun(value: Float) {
                viewModel.setLength(inKilometer = value)
                updateStepState()
            },
            enabled = fieldsEnabled
        )

        Dropdown(
            label = viewModel.phasingLabel,
            options = viewModel.phasingOptions,
            select =  fun(index: Int) {
                viewModel.setPhasing(index)
                updateStepState()
            },
            enabled = fieldsEnabled
        )

        Dropdown(
            label = viewModel.conductorLabel,
            options = viewModel.conductorOptions,
            select =  fun(index: Int) {
                viewModel.setConductor(index)
                updateStepState()
            },
            enabled = fieldsEnabled
        )

        Dropdown(
            label = viewModel.sectionLabel,
            options = viewModel.sectionOptions,
            select =  fun(index: Int) {
                viewModel.setSection(index)
                updateStepState()
            },
            enabled = fieldsEnabled
        )

        Dropdown(
            label = viewModel.intensityLabel,
            options = viewModel.intensityOptions,
            select =  fun(index: Int) {
                viewModel.setIntensity(index)
                updateStepState()
            },
            enabled = fieldsEnabled
        )

        if (isStepCompleted) { next() }
    }

}

@Composable
fun AddOutputCircuits(
    viewModel: OutputCircuitsViewModel,
    next: () -> Unit,
    skip: () -> Unit
) {
    CableForm(viewModel = viewModel, next = next)
    Button(onClick = { skip() }) {
        Text(text = viewModel.skipLabel)
    }
}

@Composable
fun EditNominalTension(viewModel: TensionViewModel, next: () -> Unit) {
    var isStepCompleted by remember {
        mutableStateOf(false)
    }
    var fieldsEnabled by remember {
        mutableStateOf(true)
    }


        fun updateStepState() {
            isStepCompleted = viewModel.isTensionDefined()
        }
        Dropdown(
            label = viewModel.tensionLabel,
            options = viewModel.tensionOptions,
            select =  fun(index: Int) {
                viewModel.setTension(index)
                updateStepState()
            },
            enabled = fieldsEnabled,
            isExpanded = true
        )

    if (isStepCompleted) next()

}


@Composable
fun EditInputCableVoltageDrop(viewModel: TruncatedInstallationSetUpViewModel, next: () -> Unit) {

    NumberInput(
        name = viewModel.inputCableVoltageDropLabel,
        select = fun(value: Float) {
            viewModel.setInputCableVoltageDrop(inVolt = value)
                                   },
        enabled = true
    )
    Button(onClick = { next() }) {

    }
}