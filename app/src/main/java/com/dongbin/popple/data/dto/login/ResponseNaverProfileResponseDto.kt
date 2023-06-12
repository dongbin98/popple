package com.dongbin.popple.data.dto.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseNaverProfileResponseDto(
    @SerializedName("id")   // 동일인 식별 정보
    @Expose
    val id: String?,

    @SerializedName("name")
    @Expose
    val name: String?,

    @SerializedName("nickname") // 사용자 별명
    @Expose
    val nickname: String,

    @SerializedName("email")    // 사용자 메일 주소
    @Expose
    val email: String?,

    @SerializedName("birthday") // 사용자 생일(MM-DD)
    @Expose
    val birthday: String?,

    @SerializedName("birthyear")    // 출생연도
    @Expose
    val birthyear: String?,
)