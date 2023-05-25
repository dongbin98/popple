package com.dongbin.popple.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoginRequest(
    val username: String,
    val password: String,
)