package com.dongbin.popple.data.api

import com.dongbin.popple.data.dto.login.PoppleLoginResponseDto
import com.dongbin.popple.data.dto.user.UserInfoResponseDto
import com.dongbin.popple.data.dto.user.UserRegisterRequestDto
import com.dongbin.popple.data.dto.user.UserRegisterWithKakaoRequestDto
import com.dongbin.popple.data.dto.user.UserRegisterResponseDto
import com.dongbin.popple.data.dto.user.UserRegisterWithNaverRequestDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    // 로그인
    @FormUrlEncoded // @FormUrlEncoded의 경우 매개변수를 @Body가 아닌 @Field로 해야한다.
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Observable<PoppleLoginResponseDto>

    // 회원가입
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("add/normal")
    fun register(@Body userRegisterRequestDto: UserRegisterRequestDto): Observable<UserRegisterResponseDto>


    // 회원가입 with Naver
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("add/naver")
    fun registerWithNaver(@Body userRegisterWithNaverRequestDto: UserRegisterWithNaverRequestDto): Observable<UserRegisterResponseDto>

    // 회원가입 with Kakao
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("add/kakao")
    fun registerWithKakao(@Body userRegisterWithKakaoRequestDto: UserRegisterWithKakaoRequestDto): Observable<UserRegisterResponseDto>

    // 아이디 중복확인
    @Headers("Accept: application/json", "content-type: application/json")
    @GET("account") // @GET의 경우 매개변수가 @Body가 아닌 @Query
    fun checkAccount(@Query("account") account: String): Observable<UserInfoResponseDto>

    // 닉네임 중복확인
    @Headers("Accept: application/json", "content-type: application/json")
    @GET("nickname")
    fun checkNickname(@Query("nickname") nickname: String): Observable<UserInfoResponseDto>
}