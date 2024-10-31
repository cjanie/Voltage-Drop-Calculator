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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.factories.InstallationFactory
import com.cjanie.voltagedropcalculator.businesslogic.factories.LineFactory
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Copper
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.usecases.InstallationSetUpUseCase
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationViewModel
import com.cjanie.voltagedropcalculator.ui.composables.commons.Header
import com.cjanie.voltagedropcalculator.ui.composables.InstallationDrawing
import com.cjanie.voltagedropcalculator.ui.composables.InstallationSetUp
import com.cjanie.voltagedropcalculator.ui.composables.VoltageDropResult
import com.cjanie.voltagedropcalculator.ui.composables.commons.SubmitButton
import com.cjanie.voltagedropcalculator.ui.theme.VoltageDropCalculatorTheme
import com.cjanie.voltagedropcalculator.ui.theme.paddingMedium

class MainActivity : ComponentActivity() {
    
    private val installationViewModel by lazy { InstallationViewModel(application) }

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




                        var installation: InstallationViewModel.InstallationPresenter? by remember {
                            //mutableStateOf(null)
                            mutableStateOf(InstallationViewModel.InstallationPresenter(installationViewModel.fakeInstallation(), installationViewModel.getApplication()))
                        }
                        var voltageDropResult: InstallationViewModel.VoltageDropResultPresenter? by remember {
                            mutableStateOf(null)
                        }

                        if (installation == null) {
                            InstallationSetUp(
                                modifier = Modifier,// contentModifier,
                                installationViewModel = installationViewModel,
                                finish = fun (installationPresenter: InstallationViewModel.InstallationPresenter) {
                                    installation = installationPresenter
                                }
                            )
                        } else {

                            ConstraintLayout(Modifier.fillMaxSize()) {
                                val (drawing, result) = createRefs()

                                InstallationDrawing(
                                    installationPresenter = installation!!,
                                    modifier = Modifier
                                        .constrainAs(drawing) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(result.top)
                                            width = Dimension.fillToConstraints
                                            height = Dimension.fillToConstraints
                                        }
                                        .fillMaxSize()
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
                                        text = installationViewModel.calculateVoltageDropLabel,
                                        onClick = { voltageDropResult = installationViewModel.voltageDropResult() },
                                        enabled = true,
                                        modifier = Modifier.constrainAs(result) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }.padding(paddingMedium, 0.dp, paddingMedium, paddingMedium)
                                    )
                                }

                            }
/*
                            Column() {
                                voltageDropResult = installationViewModel.voltageDropResult()
                                if(voltageDropResult != null) {
                                    VoltageDropResult(
                                        voltageDropResultPresenter = voltageDropResult!!,
                                    )
                                }

                                    InstallationDrawing(
                                        installationPresenter = installation!!,
                                        modifier = Modifier.fillMaxSize()
                                    )

                            }

 */




                        }
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VoltageDropCalculatorTheme {

    }
}