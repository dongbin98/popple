package com.dongbin.popple.data.api


import com.dongbin.popple.data.dto.user.UserRegisterRequestDto
import com.dongbin.popple.data.dto.user.UserRegisterResponseDto
import com.dongbin.popple.data.dto.seller.SellerInfoResponseDto
import com.dongbin.popple.data.dto.seller.SellerRegisterRequestDto
import com.dongbin.popple.data.dto.seller.SellerRegisterResponseDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SellerApi {
    // 셀러 등록
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("add")
    fun register(@Body sellerRegisterRequestDto: SellerRegisterRequestDto): Observable<SellerRegisterResponseDto>

    // 이름 중복확인
    @Headers("Accept: application/json", "content-type: application/json")
    @GET("name")
    fun checkName(@Query("name") name: String): Observable<SellerInfoResponseDto>
}