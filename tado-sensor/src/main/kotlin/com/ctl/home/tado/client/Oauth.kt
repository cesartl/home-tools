package com.ctl.home.tado.client

import com.ctl.home.tado.TadoConfig
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails

class OauthResources(
    tadoConfig: TadoConfig
) : ResourceOwnerPasswordResourceDetails() {
    init {
        scope = listOf("home.user")
        grantType = "password"
        clientSecret = tadoConfig.clientSecret
        clientId = tadoConfig.clientId
        accessTokenUri = tadoConfig.accessTokenUri
        password = tadoConfig.password
        username = tadoConfig.username
    }
}