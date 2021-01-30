package com.ctl.home.tado.client

import com.ctl.home.tado.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import feign.Feign
import feign.Logger
import feign.Param
import feign.RequestLine
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import feign.slf4j.Slf4jLogger
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext

interface TadoClient {

    @RequestLine("GET /api/v1/me")
    fun me(): TadoUser

    @RequestLine("GET /api/v2/devices/{deviceId}/temperatureOffset")
    fun getTemperatureOffset(@Param("deviceId") deviceId: String): Temperature

    @RequestLine("PUT /api/v2/devices/{deviceId}/temperatureOffset")
    fun setTemperatureOffset(
        temperature: Temperature,
        @Param("deviceId") deviceId: String
    ): Temperature

    @RequestLine("GET /api/v2/homes/{homeId}/zones/{zoneId}/state")
    fun getZoneState(@Param("homeId") homeId: String, @Param("zoneId") zoneId: String): ZoneState

    @RequestLine("GET /api/v2/homes/{homeId}/zones/{zoneId}/measuringDevice")
    fun getMeasuringDevice(@Param("homeId") homeId: String, @Param("zoneId") zoneId: String): Device

    companion object {
        private val mapper = ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        fun tadoClient(
            url: String = "https://my.tado.com",
            oauthResources: OauthResources
        ): TadoClient {
            val builder = Feign.builder()
            return builder
                .decoder(JacksonDecoder(mapper))
                .encoder(JacksonEncoder(mapper))
                .requestInterceptor(OAuth2FeignRequestInterceptor(DefaultOAuth2ClientContext(), oauthResources))
                .logLevel(Logger.Level.BASIC)
                .logger(Slf4jLogger(TadoClient::class.java))
                .target(TadoClient::class.java, url)
        }

        fun tadoClient(tadoConfig: TadoConfig): TadoClient = tadoClient(tadoConfig.tadoUrl, OauthResources(tadoConfig))
    }
}

