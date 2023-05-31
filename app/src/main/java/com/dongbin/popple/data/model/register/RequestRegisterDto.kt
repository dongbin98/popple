package com.dongbin.popple.data.model.register

data class RequestRegisterDto (
    val account: String,
    val password: String,
    val birth: String,  // 여기서 String으로 보내도 FastApi schema에서 변환
    val name: String,
    val nickname: String,
    val login_type: String,
    val created_at: String, // 여기서 String으로 보내도 FastApi schema에서 변환
)
