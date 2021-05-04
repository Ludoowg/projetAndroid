package com.example.todoandroidludovic.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoandroidludovic.network.UserInfoRepository
import com.example.todoandroidludovic.network.models.User
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserViewModel: ViewModel() {
    private val repository = UserInfoRepository()
    private val _user = MutableLiveData<User>()
    public val user: LiveData<User> = _user
    fun getUser() {
        viewModelScope.launch {
                val user = repository.getUser()
                // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            user?.let{
                _user.value = it
            }
        }
    }



    fun updateUser(user: User) {
        viewModelScope.launch {
            var updatedUser=repository.updateUser(user)
            updatedUser?.let {
                _user.value = it
            }

        }
    }

    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            var updatedUser=repository.updateAvatar(avatar)
            updatedUser?.let {
                _user.value=it
            }
        }
    }
}

