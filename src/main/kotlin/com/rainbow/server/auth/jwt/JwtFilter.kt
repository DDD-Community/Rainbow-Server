package com.rainbow.server.auth.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {

    private fun HttpServletRequest.getToken(): String? {
        return this.getHeader("Authorization")
//        rawToken
//        return if/ (rawToken != null && rawToken.startsWith("Bearer"))
//            rawToken.replace("Bearer ", "")
//        else null
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val jwt = request.getToken()

        if (jwt != null && jwtProvider.validate(jwt)) {
            SecurityContextHolder.getContext().authentication =
                jwtProvider.getAuthentication(jwt)
        }
        filterChain.doFilter(request, response)
    }
}
