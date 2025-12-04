package com.example.practica3
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
  // private const val BASE_URL = "http://localhost:8080/"
   private const val BASE_URL = "http://192.168.1.138:8080/"
   // private const val BASE_URL = "http://10.0.2.2:8080/"
    // Cookie storage
    private val cookieStore = HashMap<String, List<Cookie>>()

    private val cookieJar = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.host] ?: listOf()
        }
    }
//////////////
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .cookieJar(cookieJar)           // <-- ADD THIS LINE
        .addInterceptor(loggingInterceptor)
        .build()
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)                  // IMPORTANT!!
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}