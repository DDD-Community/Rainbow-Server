package com.rainbow.server.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoLogoutResponse(
    @JsonProperty("id")
    var id:Long
) {
}