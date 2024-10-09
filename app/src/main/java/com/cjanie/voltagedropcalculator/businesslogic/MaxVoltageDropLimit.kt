package com.cjanie.voltagedropcalculator.businesslogic
enum class ElectricitySupply {
    PUBLIC, PRIVATE
}

abstract class Use {
    abstract val PHI: Float
    abstract fun maxVoltageDropAcceptablePercentage(): Float
}

class Lighting(private val electricitySupply: ElectricitySupply): Use() {

    override val PHI: Float
        get() = 0f

    override fun maxVoltageDropAcceptablePercentage(): Float {
        return when (electricitySupply) {
            ElectricitySupply.PUBLIC -> 3f
            ElectricitySupply.PRIVATE -> 6f
        }
    }
}

class Motor(private val electricitySupply: ElectricitySupply): Use() {
    override val PHI: Float
        get() = TODO("Not yet implemented")

    override fun maxVoltageDropAcceptablePercentage(): Float {
        return when (electricitySupply) {
            ElectricitySupply.PUBLIC -> 5f
            ElectricitySupply.PRIVATE -> 8f
        }
    }
}

class MaxVoltageDropLimit(use: Use) {
    val percentage = use.maxVoltageDropAcceptablePercentage()
}

class Installation(
    use: Use,
    private val cable: Line,
    private val circuits: Array<Line>,
    nominalTension: Tension) {

    val maxVoltageDropLimit = MaxVoltageDropLimit(use)

    val voltageDrop = calculateVoltageDrop()

    val isVoltageDropAcceptable = maxVoltageDropLimit.percentage > voltageDrop.percentage(nominalTension)

    private fun calculateVoltageDrop(): VoltageDrop {
        TODO()
    }
}