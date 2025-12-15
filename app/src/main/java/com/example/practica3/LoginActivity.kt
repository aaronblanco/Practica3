package com.example.practica3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextUsername: EditText = findViewById(R.id.editTextUsername)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val buttonLogin: Button = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val user = User(username, password)
                apiService.login(user).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful && response.body()?.message == "Login successful") {
                            SessionManager.isLoggedIn = true
                            Toast.makeText(this@LoginActivity, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            var errorMessage = "Usuario o contraseña incorrectos"
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                try {
                                    val errorResponse = Gson().fromJson(errorBody, LoginErrorResponse::class.java)
                                    if (errorResponse.error == "Invalid credentials") {
                                        errorMessage = "Las credenciales no son válidas"
                                    }
                                } catch (e: Exception) {
                                    Log.e("LOGIN_ERROR_PARSE", "Error parsing error response: ${e.message}")
                                }
                            }
                            Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                            Log.e("LOGIN_ERROR", "API Error: ${response.code()} - $errorBody")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                        Log.e("LOGIN_FAILURE", "API Failure: ${t.message}")
                    }
                })
            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
