package com.ctl.home.tado.temperature

import com.pi4j.io.i2c.I2CBus
import com.pi4j.io.i2c.I2CDevice
import com.pi4j.io.i2c.I2CFactory
import kotlin.experimental.and


class Bmp280(private val offset: Double, private val address: Int) : TemperatureSensor {

    // Create I2C bus
    private val bus: I2CBus = I2CFactory.getInstance(I2CBus.BUS_1)

    // Get I2C device, BMP280 I2C address is 0x76(108)
    // Get I2C device, BMP280 I2C address is 0x76(108)
    private val device: I2CDevice = bus.getDevice(address)

    private var dig_T1: Int = 0
    private var dig_T2: Int = 0
    private var dig_T3: Int = 0

    init {
        // Create I2C bus

        // Read 24 bytes of data from address 0x88(136)

        // Read 24 bytes of data from address 0x88(136)
        val b1 = ByteArray(24)
        device.read(0x88, b1, 0, 24)

        // Convert the data
        // temp coefficents

        // Convert the data
        // temp coefficents
        dig_T1 = (b1[0] and 0xFF.toByte()) + (b1[1] and 0xFF.toByte()) * 256
        dig_T2 = (b1[2] and 0xFF.toByte()) + (b1[3] and 0xFF.toByte()) * 256
        if (dig_T2 > 32767) {
            dig_T2 -= 65536
        }
        dig_T3 = (b1[4] and 0xFF.toByte()) + (b1[5] and 0xFF.toByte()) * 256
        if (dig_T3 > 32767) {
            dig_T3 -= 65536
        }

        // Select control measurement register
        // Normal mode, temp and pressure over sampling rate = 1
        // Select control measurement register
        // Normal mode, temp and pressure over sampling rate = 1
        device.write(0xF4, 0x27.toByte())
        // Select config register
        // Stand_by time = 1000 ms
        // Select config register
        // Stand_by time = 1000 ms
        device.write(0xF5, 0xA0.toByte())
        Thread.sleep(500)
    }

    override fun temperature(): Double {
        // Read 8 bytes of data from address 0xF7(247)
        // pressure msb1, pressure msb, pressure lsb, temp msb1, temp msb, temp lsb, humidity lsb, humidity msb
        // Read 8 bytes of data from address 0xF7(247)
        // pressure msb1, pressure msb, pressure lsb, temp msb1, temp msb, temp lsb, humidity lsb, humidity msb
        val data = ByteArray(8)
        device.read(0xF7, data, 0, 8)

        // Convert pressure and temperature data to 19-bits

        // Convert pressure and temperature data to 19-bits
        val adc_t =
            ((data[3] and 0xFF.toByte()).toLong() * 65536 + (data[4] and 0xFF.toByte()).toLong() * 256 + (data[5] and 0xF0.toByte()).toLong()) / 16

        // Temperature offset calculations

        // Temperature offset calculations
        val var1 = (adc_t.toDouble() / 16384.0 - dig_T1.toDouble() / 1024.0) * dig_T2.toDouble()
        val var2 = (adc_t.toDouble() / 131072.0 - dig_T1.toDouble() / 8192.0) *
                (adc_t.toDouble() / 131072.0 - dig_T1.toDouble() / 8192.0) * dig_T3.toDouble()
        return (var1 + var2) / 5120.0 + offset
    }
}