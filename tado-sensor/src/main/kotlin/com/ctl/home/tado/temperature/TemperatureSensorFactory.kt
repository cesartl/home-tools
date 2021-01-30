package com.ctl.home.tado.temperature

import com.ctl.home.tado.TemperatureSensorConfig

object TemperatureSensorFactory {
    fun TemperatureSensorConfig.build(): TemperatureSensor {
        return when (this) {
            is TemperatureSensorConfig.Bmp280Config -> Bmp280(this.offset, this.address)
        }
    }
}