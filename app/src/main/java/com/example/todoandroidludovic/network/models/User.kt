package com.example.todoandroidludovic.network.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("email")
    var email: String,
    @SerialName("firstname")
    var firstname: String,
    @SerialName("lastname")
    var lastname: String,
    @SerialName("avatar")
    var avatar: String="https://static.wikia.nocookie.net/inazuma-eleven/images/1/10/Inazuma_Japon.jpg/revision/latest?cb=20200708181900&path-prefix=fr"
)