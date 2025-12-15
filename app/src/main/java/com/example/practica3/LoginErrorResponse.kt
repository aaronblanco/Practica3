package com.example.practica3

import com.google.gson.annotations.SerializedName

data class LoginErrorResponse(
    @SerializedName("error") val error: String
)
