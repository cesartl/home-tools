package com.ctl.home.tado

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.InputStream

@Serializable
data class Configuration(
    val tadoConfig: TadoConfig,
    val offsetThreshold: Double = 0.5,
    val temperatureSensorConfig: TemperatureSensorConfig,
    val frequency: Long = 5,
    val timeUnit: String = "MINUTES"
) {
    companion object {
        fun parse(inputStream: InputStream): Configuration {
            return inputStream.bufferedReader().use { str ->
                Yaml.default.decodeFromString(serializer(), str.readText())
            }
        }

        fun Configuration.toYaml(): String {
            return Yaml.default.encodeToString(serializer(), this)
        }
    }
}

@Serializable
data class TadoConfig(
    val username: String,
    val password: String,
    val zoneId: String,
    val tadoUrl: String = "https://my.tado.com",
    val accessTokenUri: String = "https://auth.tado.com/oauth/token",
    val clientId: String = "tado-web-app",
    val clientSecret: String = "wZaRN7rpjn3FoNyF5IFuxg9uMzYJcvOoQ8QWiIqS3hfk6gLhVlG57j5YNoZL2Rtc"
)

@Serializable
sealed class TemperatureSensorConfig {

    @Serializable
    @SerialName("bmp280")
    data class Bmp280Config(
        val address: Int = 0x77,
        val offset: Double = 0.0
    ) : TemperatureSensorConfig()
}