package com.dongbin.popple.data.dto.seller

data class SellerRegisterRequestDto (
    val id: Int,
    val seller_name: String,
    val seller_category: List<String>,  // 여기서 String으로 보내도 FastApi schema에서 변환
    val insta_account: String?,
    val youtube_account: String?,
    val website_url: String?,
    val seller_profile_image: String?,
)