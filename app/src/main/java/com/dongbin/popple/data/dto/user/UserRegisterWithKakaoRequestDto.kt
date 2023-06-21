package com.dongbin.popple.data.dto.user

data class UserRegisterWithKakaoRequestDto (
    val account: String,
    val password: String,
    val name: String,
    val login_type: String,
    val created_at: String, // 여기서 String으로 보내도 FastApi schema에서 변환
)
