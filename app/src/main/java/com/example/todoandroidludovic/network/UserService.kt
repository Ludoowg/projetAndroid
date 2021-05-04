package com.example.todoandroidludovic.network

import com.example.todoandroidludovic.network.models.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("users/info")
    suspend fun getInfo(): Response<User>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<User>

    @PATCH("users")
    suspend fun update(@Body user: User): Response<User>

}

