package com.dongbin.popple.data.api

import com.dongbin.popple.data.model.login.ResponsePoppleLoginDto
import com.dongbin.popple.data.model.login.RequestSsoLoginDto
import com.dongbin.popple.data.model.register.ResponseDuplicateCheckDto
import com.dongbin.popple.data.model.register.RequestRegisterDto
import com.dongbin.popple.data.model.register.ResponseRegisterDto
import com.dongbin.popple.data.model.register.RequestRegisterWithNaverDto
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
    @POST("login/normal")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Observable<ResponsePoppleLoginDto>

    // SSO 로그인
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("login/sso")
    fun ssoLogin(@Body requestSsoLoginDto: RequestSsoLoginDto):Observable<ResponsePoppleLoginDto>

    // 회원가입
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("add/normal")
    fun register(@Body requestRegisterDto: RequestRegisterDto): Observable<ResponseRegisterDto>


    // 회원가입 with Naver
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("add/naver")
    fun registerWithNaver(@Body registerWithNaveRequest: RequestRegisterWithNaverDto): Observable<ResponseRegisterDto>

    // 아이디 중복확인
    @Headers("Accept: application/json", "content-type: application/json")
    @GET("account") // @GET의 경우 매개변수가 @Body가 아닌 @Query
    fun checkAccount(@Query("account") account: String): Observable<ResponseDuplicateCheckDto>

    // 닉네임 중복확인
    @Headers("Accept: application/json", "content-type: application/json")
    @GET("nickname")
    fun checkNickname(@Query("nickname") nickname: String): Observable<ResponseDuplicateCheckDto>
}