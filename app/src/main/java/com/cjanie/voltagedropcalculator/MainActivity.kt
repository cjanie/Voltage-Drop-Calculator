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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.composables.commons.Header
import com.cjanie.voltagedropcalculator.ui.theme.VoltageDropCalculatorTheme
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
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
                                texts = arrayOf(voltageDropText),
                                fontWeight = FontWeight.Bold
                            )

                            val infoText = "Todo info text - requires nominal tension provider"
                            Card(
                                painter = painterResource(id = R.drawable.ic_info_24),
                                contentDescription = voltageDropViewModel.voltageDropUnit.toString(),
                                texts = arrayOf(infoText),
                                color = greenWarningColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Card(
    painter: Painter,
    contentDescription: String,
    texts: Array<String>,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
    ) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = color
        )
        for (text in texts) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 20.dp),
                color = color,
                fontWeight = fontWeight
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