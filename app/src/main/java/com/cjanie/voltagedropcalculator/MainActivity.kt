package com.cjanie.voltagedropcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.VoltageDrop
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.composables.commons.Header
import com.cjanie.voltagedropcalculator.ui.composables.InstallationDrawing
import com.cjanie.voltagedropcalculator.ui.composables.CompleteInstallationSetUpEdition
import com.cjanie.voltagedropcalculator.ui.composables.InstallationTemplate
import com.cjanie.voltagedropcalculator.ui.composables.TruncatedInstallationSetUpEdition
import com.cjanie.voltagedropcalculator.ui.composables.VoltageDropResult
import com.cjanie.voltagedropcalculator.ui.composables.commons.SubmitButton
import com.cjanie.voltagedropcalculator.ui.theme.VoltageDropCalculatorTheme
import com.cjanie.voltagedropcalculator.ui.theme.paddingMedium
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.TruncatedInstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.TruncatedInstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.viewmodels.VoltageDropViewModel

class MainActivity : ComponentActivity() {
    
    private val completeInstallationSetUpViewModel by lazy { CompleteInstallationSetUpViewModel(application) }
    private val truncatedInstallationSetUpViewModel by lazy { TruncatedInstallationSetUpViewModel(application) }

    private val voltageDropViewModel by lazy { VoltageDropViewModel()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            VoltageDropCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ){
                        Header(
                            text = getString(R.string.app_name)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(40.dp),
                            verticalArrangement = Arrangement.SpaceAround

                        ) {

                            // K
                            var sectionValue by remember {
                                mutableStateOf(voltageDropViewModel.S)
                            }
                            val sectionText = "${voltageDropViewModel.sectionText()}"
                            val materialText = "${voltageDropViewModel.materialText}"
                            Card(
                                painter = painterResource(id = R.drawable.ic_blur_linear_24),
                                contentDescription = voltageDropViewModel.sectionUnit.toString(),
                                texts = arrayOf(materialText, sectionText)
                            )

                            Slider(
                                value = sectionValue.inMillimeterSquare,
                                onValueChange = {
                                    sectionValue = Section(it)
                                    voltageDropViewModel.S = sectionValue
                                }
                            )
                            // I
                            val deviceIconId = voltageDropViewModel.deviceIconId
                            val usage = voltageDropViewModel.usage
                            var intensityValue by remember {
                                mutableStateOf(voltageDropViewModel.I)
                            }
                            val intensityText = "${voltageDropViewModel.intensityText()}"
                            Card(
                                painter = painterResource(id = deviceIconId),
                                contentDescription = usage.toString(),
                                texts = arrayOf(intensityText)
                            )
                            Slider(
                                value = intensityValue.inAmpere,
                                onValueChange = {
                                    intensityValue = Intensity(it)
                                    voltageDropViewModel.I = intensityValue
                                })
                            // L
                            var lengthValue by remember { mutableStateOf(voltageDropViewModel.L) }
                            val lengthText = "${voltageDropViewModel.lengthText()}"
                            Card(
                                painter = painterResource(id = R.drawable.ic_electrical_services_24),
                                contentDescription = voltageDropViewModel.lengthUnit.toString(),
                                texts = arrayOf(lengthText)
                            )

                            Slider(
                                value = lengthValue.inKilometer,
                                onValueChange = {
                                    lengthValue = Length(it)
                                    voltageDropViewModel.L = lengthValue
                                }
                            )

                            // delta U
                            val voltageDropText = "${voltageDropViewModel.voltageDropText()}"
                            Card(
                                painter = painterResource(id = R.drawable.ic_electric_bolt_24),
                                contentDescription = voltageDropViewModel.voltageDropUnit.toString(),
                                texts = arrayOf(voltageDropText)
                            )


                        }

                        var template by remember {
                            mutableStateOf(InstallationTemplate.TRUNCATED)
                        }
                        Button(onClick = {
                            when (template) {
                                InstallationTemplate.TRUNCATED -> template = InstallationTemplate.COMPLETE
                                InstallationTemplate.COMPLETE -> template = InstallationTemplate.TRUNCATED
                            }
                        }) {

                        }

                        // Content: installation drawing
                        // 2 modes: edit / result

                        var truncatedInstallationSetUpStep: TruncatedInstallationSetUpStep? by remember {
                            mutableStateOf(null)
                        }

                        var completeInstallationSetUpStep: CompleteInstallationSetUpStep? by remember {
                            mutableStateOf(null)
                        }

                        var installation: CompleteInstallationSetUpViewModel.CompleteInstallationPresenter by remember {
                            mutableStateOf(completeInstallationSetUpViewModel.updateInstallationPlaceHolder())
                        }

                        var truncatedInstallation: TruncatedInstallationSetUpViewModel.TruncatedInstallationPresenter by remember {
                            mutableStateOf(truncatedInstallationSetUpViewModel.updateInstallationPlaceHolder())
                        }

                        if(truncatedInstallationSetUpStep != null) {
                            TruncatedInstallationSetUpEdition(
                                viewModel = truncatedInstallationSetUpViewModel,
                                step = truncatedInstallationSetUpStep!!,
                                next = { truncatedInstallationSetUpStep = null
                                    truncatedInstallation = truncatedInstallationSetUpViewModel.updateInstallationPlaceHolder()
                                }
                            )
                        }

                        if (completeInstallationSetUpStep != null) {
                            CompleteInstallationSetUpEdition(
                                viewModel = completeInstallationSetUpViewModel,
                                step = completeInstallationSetUpStep!!,
                                next = { completeInstallationSetUpStep = null
                                        installation = completeInstallationSetUpViewModel.updateInstallationPlaceHolder()
                                }
                            )
                        }

                        var voltageDropResult: InstallationSetUpViewModel.VoltageDropResultPresenter? by remember {
                            mutableStateOf(null)
                        }


                        if (completeInstallationSetUpStep == null)
                        ConstraintLayout(Modifier.fillMaxSize()) {
                            val (drawing, result) = createRefs()

                            InstallationDrawing(
                                completeInstallationPresenter = installation,
                                truncatedInstallationPresenter = truncatedInstallation,
                                editionMode = voltageDropResult == null,
                                setInstallationSetUpStep = fun(step: CompleteInstallationSetUpStep){
                                    completeInstallationSetUpStep = step
                                },
                                installationTemplate = template,
                                modifier = Modifier
                                    .constrainAs(drawing) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(result.top)
                                        width = Dimension.fillToConstraints
                                        height = Dimension.fillToConstraints
                                    }
                                    .fillMaxSize(),
                                setTruncatedInstallationSetUpStep = fun (step: TruncatedInstallationSetUpStep) {
                                    truncatedInstallationSetUpStep = step
                                }
                            )


                            if(voltageDropResult != null) {
                                VoltageDropResult(
                                    voltageDropResultPresenter = voltageDropResult!!,
                                    modifier = Modifier.constrainAs(result) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                    }
                                )
                            } else {
                                SubmitButton(
                                    text = truncatedInstallationSetUpViewModel.calculateVoltageDropLabel,
                                    onClick = { voltageDropResult = truncatedInstallationSetUpViewModel.voltageDropResult() },
                                    enabled = truncatedInstallationSetUpViewModel.isSetUpComplete(),
                                    modifier = Modifier
                                        .constrainAs(result) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }
                                        .padding(paddingMedium)
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
fun Card(painter: Painter, contentDescription: String, texts: Array<String>) {
    Row {
        Icon(
            painter = painter,
            contentDescription = contentDescription
        )
        for (text in texts) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VoltageDropCalculatorTheme {

    }
}