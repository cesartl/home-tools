package com.ctl.home.tado

import com.ctl.home.tado.client.TadoClient
import com.ctl.home.tado.temperature.TemperatureSensorFactory.build
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TadoSensorApp {

    fun run(configuration: Configuration) {
        val pool = Executors.newScheduledThreadPool(1)
        val tadoClient = TadoClient.tadoClient(configuration.tadoConfig)
        val sensor = configuration.temperatureSensorConfig.build()

        val homeId = tadoClient.me().homeId

        val tadoSensor = TadoSensor(
            temperatureSensor = sensor,
            tadoClient = tadoClient,
            homeId = homeId,
            zoneId = configuration.tadoConfig.zoneId,
            threshold = configuration.offsetThreshold
        )
        val timeUnit = TimeUnit.valueOf(configuration.timeUnit)
        val future = pool.scheduleWithFixedDelay(tadoSensor, 0, configuration.frequency, timeUnit)

        try {
            future.get()
        } catch (e: Exception) {
            log.error("Error in main", e)
        }
        pool.shutdown()
        pool.awaitTermination(10, TimeUnit.SECONDS)
    }

    companion object {

        private val log = LoggerFactory.getLogger(TadoSensorApp::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            val configProperty = "config.path"
            val configPath = System.getProperty(configProperty) ?: error("Missing property $configProperty")
            val conf = Configuration.parse(File(configPath).inputStream())
            TadoSensorApp().run(conf)
        }
    }
}