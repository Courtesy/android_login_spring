package com.courtesy.login_spring_example.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://xxx.xxx.xxx.xxx:8080/" // TODO Replace with your computer backend address

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface LoginApiService {
    @POST("user/create")
    suspend fun createUser(@Body post: User) : Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body post: User) : Response<LoginResponse>

    @GET("user")
    suspend fun getUser(@Header("Authorization") authHeader: String) : Response<SpringUser>

    @POST("auth/refresh")
    suspend fun getNewAccessToken(@Body refreshRequestToken: RefreshRequest) : Response<RefreshResponse>

    @GET("logout")
    suspend fun logout(@Header("Authorization") authHeader: String) : Response<Unit>
}

object LoginApi {
    val retrofitService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}