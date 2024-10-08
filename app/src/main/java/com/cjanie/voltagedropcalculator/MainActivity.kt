package com.cjanie.voltagedropcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

        val calculatorModel = CalculatorModel()
        val formPresenter = FormPresenter(this, calculatorModel)
        val resultPresenter = ResultPresenter(this)
        
        enableEdgeToEdge()
        setContent {
            VoltageDropCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(modifier = Modifier
                        .padding(innerPadding)
                    ){
                        Header(
                            text = getString(R.string.app_name)
                        )

                        var voltageDropResult: CalculatorModel.VoltageDropResult? by remember {
                            mutableStateOf(null)
                        }

                        // Form
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            var isFormComplete by remember {
                                mutableStateOf(false)
                            }

                            NumberInput(
                                name = formPresenter.lengthLabel,
                                select =  fun(value: Float) {
                                    calculatorModel.setLength(inKilometer = value)
                                    isFormComplete = !calculatorModel.isNullValue()
                                },
                                enabled = voltageDropResult == null
                            )

                            Dropdown(
                                name = formPresenter.functionalContextLabel,
                                options = formPresenter.functionalContexts,
                                select =  fun(index: Int) {
                                    calculatorModel.setFunctionalContext(index)
                                    isFormComplete = !calculatorModel.isNullValue()
                                },
                                enabled = voltageDropResult == null
                            )

                            Dropdown(
                                name = formPresenter.lineTypeLabel,
                                options = formPresenter.lineTypeOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setLineType(index)
                                    isFormComplete = !calculatorModel.isNullValue()
                                },
                                enabled = voltageDropResult == null
                            )

                            Dropdown(
                                name = formPresenter.conductorLabel,
                                options = formPresenter.conductorOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setConductor(index)
                                    isFormComplete = !calculatorModel.isNullValue()
                                },
                                enabled = voltageDropResult == null
                            )

                            Dropdown(
                                name = formPresenter.sectionLabel,
                                options = formPresenter.sectionOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setSection(index)
                                    isFormComplete = !calculatorModel.isNullValue()
                                },
                                enabled = voltageDropResult == null
                            )
                            
                            Dropdown(
                                name = formPresenter.currentIntensityLabel,
                                options = formPresenter.currentIntensityOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setIntensity(index)
                                    isFormComplete = !calculatorModel.isNullValue()
                                },
                                enabled = voltageDropResult == null
                            )
                            
                            Dropdown(
                                name = formPresenter.tensionLabel,
                                options = formPresenter.tensionOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setTension(index)
                                    isFormComplete = !calculatorModel.isNullValue()
                                },
                                enabled = voltageDropResult == null
                            )

                            if(voltageDropResult == null) {
                                SubmitButton(
                                    text = formPresenter.calculateVoltageDropPercentageLabel,
                                    onClick = {
                                        try {
                                            voltageDropResult = calculatorModel.calculateVoltageDrop()
                                        } catch (e: NullValueException) {
                                            e.printStackTrace()
                                        }
                                    },
                                    enabled = isFormComplete,
                                )
                            }



                        // Result
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
}

@Composable
fun Result(resultPresenter: ResultPresenter, voltageDropResult: CalculatorModel.VoltageDropResult) {
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