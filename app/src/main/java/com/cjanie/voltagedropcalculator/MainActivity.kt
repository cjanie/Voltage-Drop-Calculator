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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.composables.Dropdown
import com.cjanie.voltagedropcalculator.ui.composables.Header
import com.cjanie.voltagedropcalculator.ui.composables.LabeledText
import com.cjanie.voltagedropcalculator.ui.composables.NumberInput
import com.cjanie.voltagedropcalculator.ui.composables.SubmitButton
import com.cjanie.voltagedropcalculator.ui.composables.Title
import com.cjanie.voltagedropcalculator.ui.theme.VoltageDropCalculatorTheme
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.onGreenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.onRedWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.onWhiteColor
import com.cjanie.voltagedropcalculator.ui.theme.redWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.whiteColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val formPresenter = FormPresenter(this, FormModel())
        val resultPresenter = ResultPresenter(this)
        
        enableEdgeToEdge()
        setContent {
            VoltageDropCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                    ){
                        Header(
                            text = getString(R.string.app_name)
                        )

                        var voltageDropResult: FormModel.VoltageDropResult? by remember {
                            mutableStateOf(null)
                        }

                        Form(
                            formPresenter = formPresenter,
                            fieldsEnabled = voltageDropResult == null,
                            setResult = fun (result: FormModel.VoltageDropResult) {
                                voltageDropResult = result
                            }
                        )

                        if(voltageDropResult != null) {
                            Result(
                                resultPresenter = resultPresenter,
                                voltageDropResult = voltageDropResult!!
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable()
fun InstallationSetUp() {

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
fun Result(resultPresenter: ResultPresenter, voltageDropResult: FormModel.VoltageDropResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (voltageDropResult.isVoltageDropAcceptable) greenWarningColor else redWarningColor),
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
                label = resultPresenter.voltageDropInVoltLabel,
                text = resultPresenter.voltageDropInVoltAsString(voltageDropResult.inVolt),
                textColor = onWhiteColor
            )
            LabeledText(
                label = resultPresenter.voltageDropPercentageLabel,
                text = resultPresenter.percentageAsString(voltageDropResult.percentage),
                textColor = onWhiteColor
            )
        }

        val textColor = if (voltageDropResult.isVoltageDropAcceptable) onGreenWarningColor else onRedWarningColor

        Title(
            name = resultPresenter.isVoltageDropAcceptableAsString(voltageDropResult.isVoltageDropAcceptable),
            textColor = textColor
        )

        LabeledText(
            label = resultPresenter.maxVoltageDropAcceptablePercentageLabel,
            text = resultPresenter.percentageAsString(voltageDropResult.maxVoltageDropAcceptablePercentage),
            textColor = textColor
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VoltageDropCalculatorTheme {

    }
}