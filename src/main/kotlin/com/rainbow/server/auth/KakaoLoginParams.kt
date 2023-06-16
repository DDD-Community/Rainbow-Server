package com.rainbow.server.auth

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class KakaoLoginParams(
    var authorizationCode: String = ""
) {
    fun makeBody(): MultiValueMap<String, String> {
        val body: MultiValueMap<String, String> = LinkedMultiValueMap()
        body.add("code", authorizationCode)
        return body
    }
}