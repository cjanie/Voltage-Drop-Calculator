package com.cjanie.voltagedropcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.theme.Typography
import com.cjanie.voltagedropcalculator.ui.theme.VoltageDropCalculatorTheme
import com.cjanie.voltagedropcalculator.ui.theme.copperColor
import com.cjanie.voltagedropcalculator.ui.theme.onCopperColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calculatorModel = CalculatorModel()
        val formPresenter = FormPresenter(this, calculatorModel)

        
        enableEdgeToEdge()
        setContent {
            VoltageDropCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Header(
                            text = getString(R.string.app_name)
                        )

                        Column {

                            LabeledDropdown(
                                name = formPresenter.functionalContextLabel,
                                options = formPresenter.functionalContexts,
                                select =  fun(index: Int) {
                                    calculatorModel.setFunctionnalContext(index)
                                }
                            )

                            LabeledDropdown(
                                name = formPresenter.lineTypeLabel,
                                options = formPresenter.lineTypeOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setLineType(index)
                                }
                            )

                            LabeledDropdown(
                                name = formPresenter.conductorLabel,
                                options = formPresenter.conductorOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setConductor(index)
                                }
                            )

                            LabeledDropdown(
                                name = formPresenter.sectionLabel,
                                options = formPresenter.sectionOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setSection(index)
                                }
                            )
                            
                            LabeledDropdown(
                                name = formPresenter.currentIntensityLabel,
                                options = formPresenter.currentIntensityOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setIntensity(index)
                                }
                            )
                            
                            LabeledDropdown(
                                name = formPresenter.currentIntensityLabel,
                                options = formPresenter.tensionOptions,
                                select =  fun(index: Int) {
                                    calculatorModel.setTension(index)
                                }
                            )

                            Label(name = formPresenter.lengthLabel)
                            NumberInput(
                                select =  fun(value: Float) {
                                    calculatorModel.lineLength = value
                                }
                            )

                            var voltageDropInVolt: Float? by remember {
                                mutableStateOf(null)
                            }

                            var voltageDropInPercentage: Float? by remember {
                                mutableStateOf(null)
                            }
                            
                            var isVoltageDropAcceptable: Boolean? by remember {
                                mutableStateOf(null)
                            }
                            Button(onClick = {
                                try {
                                    voltageDropInVolt = calculatorModel.calculateVoltageDropInVolt()
                                    voltageDropInPercentage = calculatorModel.calculateVoltageDropInPercentage()
                                    isVoltageDropAcceptable = calculatorModel.isVoltageDropAcceptable()
                                } catch (e: NullValueException) {

                                }
                            }) {
                                Text(text = formPresenter.calculateVoltageDropPercentageLabel)
                            }

                            if(voltageDropInVolt != null) {
                                Label(name = formPresenter.voltageDropInVoltLabel)
                                Text(text = formPresenter.voltageDropInVoltAsString(voltageDropInVolt!!))
                            }

                            if(voltageDropInPercentage != null) {
                                Label(name = formPresenter.voltageDropPercentageLabel)
                                Text(text = formPresenter.percentageAsString(voltageDropInPercentage!!))
                            }
                            
                            if (isVoltageDropAcceptable != null) {
                                Text(text = formPresenter.isVoltageDropAcceptableAsString(isVoltageDropAcceptable!!))
                            }



                            


                        }

                    }

                }
            }
        }
    }
}

@Composable
fun Header(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = copperColor)
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center
        ) {
        Title(name = text, textColor = onCopperColor)
    }
}

@Composable
fun Title(name: String, textColor: Color) {
    val textStyle = Typography.titleLarge
    Text(
        text = name,
        fontFamily = textStyle.fontFamily,
        fontWeight = textStyle.fontWeight,
        fontSize = textStyle.fontSize,
        lineHeight = textStyle.lineHeight,
        letterSpacing = textStyle.letterSpacing,
        color = textColor
    )
}

@Composable
fun LabeledDropdown(name: String, options: Array<String>, select: (itemPosition: Int) -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        Label(name = name)
        Dropdown(options = options, select)
    }
}

@Composable
fun Label(name: String) {
    val textStyle: TextStyle = Typography.labelLarge
    Text(
        text = name,
        fontFamily = textStyle.fontFamily,
        fontWeight = textStyle.fontWeight,
        fontSize = textStyle.fontSize,
        lineHeight = textStyle.lineHeight,
        letterSpacing = textStyle.letterSpacing
    )
}



@Composable
fun Dropdown(options: Array<String>, select: (itemPosition: Int) -> Unit) {
    var isDropDownExpanded by remember {
        mutableStateOf(false)
    }

    var itemPosition by remember {
        mutableStateOf(0)
    }

    select(itemPosition)

    Button(
        onClick = { isDropDownExpanded = true },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonColors(
            containerColor = copperColor,
            contentColor = onCopperColor,
            disabledContainerColor = copperColor,
            disabledContentColor = onCopperColor)
    ) {
        Text(text = options[itemPosition])
    }
    DropdownMenu(
        expanded = isDropDownExpanded,
        onDismissRequest = { isDropDownExpanded = false },
        ) {
        options.forEachIndexed { index, option ->
            DropdownMenuItem(
                text = { Text(text = option) },
                onClick = {
                    isDropDownExpanded = false
                    itemPosition = index
                }
            )
        }
    }

}

@Composable
fun NumberInput(select: (value: Float) -> Unit) {
    var value by remember {
        mutableStateOf("")
    }
    if(value.isNotEmpty()) select(value.toFloat())


    TextField(
        value = value,
        onValueChange = { value = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VoltageDropCalculatorTheme {

    }
}