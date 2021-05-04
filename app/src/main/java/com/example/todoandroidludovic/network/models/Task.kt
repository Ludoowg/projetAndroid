package com.example.todoandroidludovic.network.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Task(
    @SerialName("description")
    val description: String="",
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String
):Parcelable