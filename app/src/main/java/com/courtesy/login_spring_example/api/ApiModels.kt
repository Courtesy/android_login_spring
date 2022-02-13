package com.courtesy.login_spring_example.api

data class User(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshRequest(
    val refreshToken: String
)

data class RefreshResponse(
    val accessToken: String
)

data class DefaultErrorResponse(
    val status: Int,
    val message: String
)

data class SpringUser(
    val id: String,
    val username: String,
    val password: String,
    val accountNonExpired: Boolean,
    val credentialsNonExpired: Boolean
)