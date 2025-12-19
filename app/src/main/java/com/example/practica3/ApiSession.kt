package com.example.practica3

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object ApiSession {
    private val gson = Gson()
    private val client = ApiClient.okHttpClient
    private val jsonMedia = "application/json; charset=utf-8".toMediaType()

    data class LoginRequest(val username: String, val password: String)

    suspend fun login(username: String, password: String): Result<Boolean> {
        return try {
            val bodyJson = gson.toJson(LoginRequest(username, password))
            val request = Request.Builder()
                .url("https://dss-app-deezm.ondigitalocean.app/api/login")
                .post(bodyJson.toRequestBody(jsonMedia))
                .build()

            client.newCall(request).execute().use { resp ->
                if (resp.isSuccessful) {
                    Result.success(true)
                } else {
                    val msg = resp.body?.string().orEmpty()
                    Result.failure(IllegalStateException("Login failed: ${resp.code} ${msg}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduct(product: Product): Result<Product> {
        return try {
            val bodyJson = gson.toJson(product)
            val request = Request.Builder()
                .url("https://dss-app-deezm.ondigitalocean.app/api/products")
                .post(bodyJson.toRequestBody(jsonMedia))
                .build()

            client.newCall(request).execute().use { resp ->
                val respBody = resp.body?.string().orEmpty()
                if (resp.isSuccessful) {
                    val created = gson.fromJson(respBody, Product::class.java)
                    Result.success(created)
                } else {
                    Result.failure(IllegalStateException("createProduct failed: ${resp.code} ${respBody}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

