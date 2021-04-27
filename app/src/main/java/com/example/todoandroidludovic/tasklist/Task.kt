package com.example.todoandroidludovic.tasklist

import java.io.Serializable

data class Task  (
    val id: String="",
    val title: String ="",
    val text_description: String ="Rien à signaler"
):Serializable