package com.dongbin.popple.data.dto.register

data class RequestRegisterWithKakaoDto (
    val account: String,
    val name: String,
    val login_type: String,
    val created_at: String, // 여기서 String으로 보내도 FastApi schema에서 변환
)
