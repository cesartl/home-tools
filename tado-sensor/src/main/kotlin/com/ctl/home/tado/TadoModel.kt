package com.ctl.home.tado

data class TadoUser(
    val name: String,
    val id: String,
    val homeId: String
)

data class Temperature(
    val celsius: Double? = null,
    val fahrenheit: Double? = null
)

data class Device(
    val serialNo: String
)


data class ZoneState(
    val setting: ZoneStateSetting,
    val sensorDataPoints: SensorDataPoints
)

data class SensorDataPoints(
    val insideTemperature: Temperature
)

data class ZoneStateSetting(
    val temperature: Temperature
)