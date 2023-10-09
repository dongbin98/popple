package com.dongbin.popple.data.dto.seller

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SellerRegisterResponseDto (
    @SerializedName("id")
    @Expose
    val id: String?,

    @SerializedName("name")
    @Expose
    val name: String?,
)