package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionnalContext
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.factories.LineFactory
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.businesslogic.factories.InstallationFactory
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension

class FormModel {

    val functionnalContextValues: Set<FunctionnalContext> = FunctionnalContext.values().toHashSet()

    val electricitySupplyValues: Set<ElectricitySupply> = ElectricitySupply.values().toHashSet()

    val phasingValues: Set<Phasing> = Phasing.values().toHashSet()

    val conductorMaterialValues: Set<ConductorMaterial> = ConductorMaterial.values().toHashSet()

    val sectionValues: Set<Section> = setOf(
        Section(inMillimeterSquare = 1.5f),
        Section(inMillimeterSquare = 2.5f),
        Section(inMillimeterSquare = 4f),
        Section(inMillimeterSquare = 6f),
        Section(inMillimeterSquare = 10f),
        Section(inMillimeterSquare = 16f),
        Section(inMillimeterSquare = 25f),
        Section(inMillimeterSquare = 35f),
        Section(inMillimeterSquare = 50f),
        Section(inMillimeterSquare = 70f),
        Section(inMillimeterSquare = 95f),
        Section(inMillimeterSquare = 120f),
        Section(inMillimeterSquare = 150f),
        Section(inMillimeterSquare = 185f),
        Section(inMillimeterSquare = 240f),
        Section(inMillimeterSquare = 300f)
    )

    val intensityValues: Set<Intensity> = setOf(
        Intensity(inAmpere = 20f),
        Intensity(inAmpere = 100f),
        Intensity(inAmpere = 150f),
        Intensity(inAmpere = 500f)
    )

    val tensionValues: Set<Tension> = setOf(
        Tension(inVolt = 3f),
        Tension(24f),
        Tension(230f),
    )

    private var functionnalContext: FunctionnalContext? = null
    private var electricitySupply: ElectricitySupply? = null
    private var phasing: Phasing? = null
    private var conductorMaterial: ConductorMaterial? = null
    private var section: Section? = null
    private var intensity: Intensity? = null
    private var tension: Tension? = null
    private var length: Length? = null

    private var installation: Installation? = null

    fun setFunctionalContext(itemPosition: Int) {
        functionnalContext = functionnalContextValues.toList()[itemPosition]
    }

    fun setElectricitySupply(itemPosition: Int) {
        electricitySupply = electricitySupplyValues.toList()[itemPosition]
    }

    fun setPhasing(itemPosition: Int) {
        phasing = phasingValues.toList()[itemPosition]
    }

    fun setConductor(itemPosition: Int) {
        conductorMaterial = conductorMaterialValues.toList()[itemPosition]
    }

    fun setSection(itemPosition: Int) {
        section = sectionValues.toList()[itemPosition]
    }

    fun setIntensity(itemPosition: Int) {
        intensity = intensityValues.toList()[itemPosition]
    }

    fun setTension(itemPosition: Int) {
        tension = tensionValues.toList()[itemPosition]
    }

    fun setLength(inKilometer: Float) {
        length = Length(inKilometer)
    }

    private fun setUpInstallation() {
        if (isNullValue())
            throw NullValueException()

        installation = InstallationFactory.installation(
            functionnalContext = functionnalContext!!,
            electricitySupply = electricitySupply!!,
            cable = LineFactory.line(
                functionnalContext = functionnalContext!!,
                electricitySupply = electricitySupply!!,
                phasing = phasing!!,
                conductorMaterial = conductorMaterial!!,
                section = section!!,
                intensity = intensity!!,
                length = length!!
                ),
            nominalTension = tension!!
        )
    }

    class VoltageDropResult(
        val inVolt: Float,
        val percentage: Float,
        val isVoltageDropAcceptable: Boolean,
        val maxVoltageDropAcceptablePercentage: Float
    )

    fun calculateVoltageDrop(): VoltageDropResult {
        return VoltageDropResult(
            inVolt = calculateVoltageDropInVolt(),
            percentage = calculateVoltageDropInPercentage()!!,
            isVoltageDropAcceptable = isVoltageDropAcceptable()!!,
            maxVoltageDropAcceptablePercentage = maxVoltageDropPercentageAcceptable()!!
        )
    }

    private fun calculateVoltageDropInVolt(): Float {
        if (installation == null) setUpInstallation()
        return installation?.voltageDropInVolt!!
    }

    private fun calculateVoltageDropInPercentage(): Float? {
        if (installation == null) setUpInstallation()
        return installation?.voltageDropPercentage

    }

    private fun isVoltageDropAcceptable(): Boolean? {
        if (installation == null) setUpInstallation()
        return installation?.isVoltageDropAcceptable
    }

    private fun maxVoltageDropPercentageAcceptable(): Float? {
        if (installation == null) setUpInstallation()
        return installation?.maxVoltageDropLimitPercentage
    }

    fun isNullValue(): Boolean {
        return functionnalContext == null ||
                electricitySupply == null ||
                phasing == null ||
                conductorMaterial == null ||
                section == null ||
                intensity == null ||
                tension == null ||
                length == null
    }

}