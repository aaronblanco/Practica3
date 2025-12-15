package com.example.practica3

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message") val message: String
)
