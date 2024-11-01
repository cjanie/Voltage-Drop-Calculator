package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.cjanie.voltagedropcalculator.ui.composables.commons.Title
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.viewmodels.CableViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.ElectricitySupplyViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.InputCableViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.OutputCircuitsViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.TensionViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.UsageViewModel

@Composable()
fun InstallationSetUpEdition(
    modifier: Modifier = Modifier.fillMaxSize(),
    installationViewModel: InstallationViewModel,
    step: InstallationSetUpStep,
    next: () -> Unit
){
            when (step) {

                InstallationSetUpStep.DEFINE_USAGE -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Bottom
                ){
                    EditUsage(
                        viewModel = installationViewModel,
                        next = { next() },
                    )
                }
                InstallationSetUpStep.ADD_INPUT_CABLE -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Center
                ){
                    Subtitle(
                        text = installationViewModel.stepLabel(step),
                        color = greenWarningColor
                    )
                    AddInputCable(
                        viewModel = installationViewModel.inputCableViewModel,
                        next = { next() }
                    )
                }
                InstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Center
                ){
                    Subtitle(
                        text = installationViewModel.stepLabel(step),
                        color = greenWarningColor
                    )
                    AddOutputCircuits(
                        viewModel = installationViewModel.outputCircuitsViewModel,
                        next = { next() },
                        skip = { next() }
                    )

                }
                InstallationSetUpStep.DEFINE_NOMINAL_TENSION -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Top
                ){
                    EditNominalTension(
                        viewModel = installationViewModel,
                        next = { next() }
                    )
                }

                InstallationSetUpStep.DEFINE_ELECTRICITY_SUPPLY -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Top
                ) {
                    EditElectricitySupply(
                        viewModel = installationViewModel,
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