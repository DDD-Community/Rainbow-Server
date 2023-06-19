package com.rainbow.server.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class KakaoApiClient(private val restTemplate: RestTemplate)  {

    companion object {
        private const val GRANT_TYPE = "authorization_code"
    }

    @Value("\${oauth.kakao.url.auth}")
    private lateinit var authUrl: String

    @Value("\${oauth.kakao.url.api}")
    private lateinit var apiUrl: String

    @Value("\${oauth.kakao.client-id}")
    private lateinit var clientId: String

    @Value("\${oauth.kakao.client-secret}")
    private lateinit var secret: String

    fun requestAccessToken(code: String): String {
        val url = "$authUrl/oauth/token"

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED

//        val body = params.makeBody()
        val body: MultiValueMap<String, String> = LinkedMultiValueMap()
        body.add("code", code)
        println("위body? $body")


        body.add("grant_type", GRANT_TYPE)
        body.add("client_id", clientId)
        body.add("client_secret",secret)
        body.add("redirect_uri","http://43.201.219.27:8080/auth/kakao")
//        body.add("redirect_uri","http://localhost:8080/auth/kakao")

        val request = HttpEntity(body, httpHeaders)

        println("밑body? $body")
        println("headr? $httpHeaders")
        restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()

        val response = restTemplate.postForObject(url, request, KakaoTokens::class.java)
            ?: throw IllegalStateException("KakaoTokens response is null")

        println(response)

        return response.accessToken
    }

    fun requestOauthInfo(accessToken: String): KakaoInfoResponse {
        val url = "$apiUrl/v2/user/me"

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        httpHeaders.set("Authorization", "Bearer $accessToken")

        val body = LinkedMultiValueMap<String, String>()
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]")

        val request = HttpEntity(body, httpHeaders)


        return restTemplate.postForObject(url, request, KakaoInfoResponse::class.java)
            ?: throw IllegalStateException("KakaoInfoResponse is null")
    }

    fun logout(accessToken: String): KakaoUserLogout {
        val url = "$apiUrl/v1/user/logout"

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        httpHeaders.set("Authorization", "Bearer $accessToken")

        val body = LinkedMultiValueMap<String, String>()

        val request = HttpEntity(body, httpHeaders)

        return restTemplate.postForObject(url, request, KakaoUserLogout::class.java)
            ?: throw IllegalStateException("KakaoInfoResponse is null")
    }
}
