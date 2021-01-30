package com.ctl.home.tado

import com.ctl.home.tado.client.TadoClient
import com.ctl.home.tado.temperature.TemperatureSensor
import org.slf4j.LoggerFactory
import kotlin.math.absoluteValue

class TadoSensor(
    private val temperatureSensor: TemperatureSensor,
    private val tadoClient: TadoClient,
    private val homeId: String,
    private val zoneId: String,
    private val threshold: Double
) : Runnable {
    override fun run() {
        try {
            val device = tadoClient.getMeasuringDevice(homeId, zoneId)

            val measuredTemperature = temperatureSensor.temperature()
            log.info("Measured room temperature is $measuredTemperature °C")

            val existingOffset = tadoClient.getTemperatureOffset(device.serialNo)
            val zoneState = tadoClient.getZoneState(homeId, zoneId)
            val tadoTemperature = (zoneState.sensorDataPoints.insideTemperature.celsius ?: 0.0)
            log.info("Tado room temperature is $tadoTemperature °C")

            val offsetDelta = measuredTemperature - tadoTemperature
            val targetOffset = (existingOffset.celsius ?: 0.0) + offsetDelta

            if (offsetDelta.absoluteValue > threshold) {
                log.info("Existing offset: ${existingOffset.celsius}. Need to change offset by $offsetDelta. Setting offset to $targetOffset")
                val response = tadoClient.setTemperatureOffset(Temperature(celsius = targetOffset), device.serialNo)
                log.info("Successfully set offset to ${response.celsius} °C")
            } else {
                log.info("Existing offset: ${existingOffset.celsius}. Offset delta $offsetDelta is below threshold $threshold")
            }
        } catch (e: Exception) {
            log.error("Could not run TadoSensor", e)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(TadoSensor::class.java)
    }
}