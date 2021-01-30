package com.ctl.home.tado

import com.ctl.home.tado.Configuration.Companion.toYaml
import org.junit.Test


internal class ConfigurationTest{
    @Test
    fun sampleConfig() {
        val config = Configuration(
            offsetThreshold = 0.5,
            tadoConfig = TadoConfig(
                username = "<username>",
                password = "password",
                zoneId = "123"
            ),
            temperatureSensorConfig = TemperatureSensorConfig.Bmp280Config()
        )

        println(config.toYaml())
    }
}