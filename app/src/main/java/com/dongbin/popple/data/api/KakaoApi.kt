package com.dongbin.popple.data.api

import com.dongbin.popple.data.dto.login.ResponseKakaoProfileDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

interface KakaoApi {
    // 프로필 조회
    @Headers("Accept: application/json", "content-type: application/json")
    @GET("me")
    fun getUserProfile(): Observable<ResponseKakaoProfileDto>
}