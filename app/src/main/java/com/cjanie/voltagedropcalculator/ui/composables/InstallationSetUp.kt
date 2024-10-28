package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cjanie.voltagedropcalculator.ui.composables.commons.Dropdown
import com.cjanie.voltagedropcalculator.ui.composables.commons.Label
import com.cjanie.voltagedropcalculator.ui.composables.commons.NumberInput
import com.cjanie.voltagedropcalculator.ui.composables.commons.Subtitle
import com.cjanie.voltagedropcalculator.ui.composables.commons.Title
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.viewmodels.CableViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.InputCableViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.OutputCircuitsViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.TensionViewModel

@Composable()
fun InstallationSetUp(
    modifier: Modifier,
    installationViewModel: InstallationViewModel,
    finish: (installationPresenter: InstallationViewModel.InstallationPresenter) -> Unit
){

    Column(
        modifier = modifier
    ) {

        Title(
            text = installationViewModel.title,
            textColor = copperColor
        )

        var step: InstallationSetUpStep? by remember {
            mutableStateOf(installationViewModel.installationSetUpStart)
        }


        if(step != null) {

            Subtitle(
                text = installationViewModel.stepLabel(step!!),
                color = greenWarningColor
            )

            fun next() {
                step = installationViewModel.next(step!!)
            }

            when (step!!) {

                InstallationSetUpStep.DEFINE_USAGE -> DefineInstallationUsage(
                    viewModel = installationViewModel,
                    next = { next() }
                )
                InstallationSetUpStep.ADD_INPUT_CABLE -> AddInputCable(
                    viewModel = installationViewModel.inputCableViewModel,
                    next = { next() }
                )
                InstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> AddOutputCircuits(
                    viewModel = installationViewModel.outputCircuitsViewModel,
                    next = { next() },
                    skip = { next() }
                )
                InstallationSetUpStep.DEFINE_NOMINAL_TENSION -> DefineNominalTension(
                    viewModel = installationViewModel,
                    next = {
                        next()
                        finish(installationViewModel.setUp())
                    }
                )
            }
        }
    }
}

@Composable
fun DefineInstallationUsage(viewModel: InstallationViewModel, next: () -> Unit) {

    var isStepCompleted by remember {
        mutableStateOf(false)
    }

    var isStepEnabled by remember {
        mutableStateOf(true)
    }

    Column {

        fun updateStepState() {
            isStepCompleted = viewModel.isUsageDefined()
        }

        Dropdown(
            label = viewModel.functionalContextLabel,
            options = viewModel.functionnalContextOptions,
            select = fun(itemPosition: Int) {
                viewModel.setFunctionnalContext(itemPosition)
                updateStepState()
            },
            enabled = isStepEnabled
        )
        Dropdown(
            label = viewModel.electricitySupplyLabel,
            options = viewModel.electricitySupplyOptions,
            select = fun(index: Int) {
                viewModel.setElectricitySupply(index)
                updateStepState()
            },
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

    Column {

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
            enabled = fieldsEnabled
        )
    }

    if (isStepCompleted) next()


}