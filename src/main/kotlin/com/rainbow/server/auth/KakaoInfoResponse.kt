package com.rainbow.server.auth
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoInfoResponse(
    @JsonProperty("kakao_account")
    var kakaoAccount: KakaoAccount = KakaoAccount()
) {
    val email: String
        get() = kakaoAccount.email

    val nickname: String
        get() = kakaoAccount.profile.nickname

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KakaoAccount(
        var profile: KakaoProfile = KakaoProfile(),
        var email: String = ""
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KakaoProfile(
        var nickname: String = ""
    )
}
