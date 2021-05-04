package com.example.todoandroidludovic.network

import androidx.lifecycle.MutableLiveData
import com.example.todoandroidludovic.network.models.User
import okhttp3.MultipartBody

class UserInfoRepository {
    var userWebService=Api.userService
    private val _user = MutableLiveData<User>()

    suspend fun getUser():User?{
        return userWebService.getInfo().body()
    }

    suspend fun updateUser(user: User):User?{
        val res= userWebService.update(user)
        return if (res.isSuccessful) res.body() else null
    }
    suspend fun updateAvatar(avatar: MultipartBody.Part):User?{
        var res= userWebService.updateAvatar(avatar)
      return  if(res.isSuccessful)  res.body() else return null
    }

}