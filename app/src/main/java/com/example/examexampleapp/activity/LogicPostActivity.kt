package com.example.examexampleapp.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.examexampleapp.MainActivity
import com.example.examexampleapp.R
import com.example.examexampleapp.SharedPreferenceManager
import com.example.examexampleapp.api.MyAPI
import com.example.examexampleapp.databinding.ActivityLogicPostBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.internal.wait
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LogicPostActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLogicPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logic_post)

        val shrdPrefManager = SharedPreferenceManager(this)
        Toast.makeText(this, "JWT: ${shrdPrefManager.JWT}",
            Toast.LENGTH_LONG).show()

        // Binding
        binding = ActivityLogicPostBinding.inflate(layoutInflater)
        val view = binding.root

        binding.back.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
            finish()
        }

        binding.sendCode.setOnClickListener {
            sendCode()
        }

        binding.signIn.setOnClickListener {
            signIn()
        }

        // Set the view
        setContentView(view)
    }

    private fun sendCode(){

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor).build()

        val gsonBuilder = GsonBuilder()
            .setLenient().create()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .baseUrl(getString(R.string.api_root))
            .client(httpClient).build()

        val requestApi = retrofit.create(MyAPI::class.java )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                requestApi.postSendEmail(binding.emailEdt.text.toString())
                    .awaitResponse()
                Log.d("Response", "Success send email")
            } catch (e: java.lang.Exception) {
                Log.d(ContentValues.TAG, e.toString())
            }
        }
    }

    private fun signIn(){
        val shrdPrefManager = SharedPreferenceManager(this)

        // Перехватчик http запросов
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        // Клиент перехватчик
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor).build()

        val gsonBuilder = GsonBuilder()
            .setLenient().create()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .baseUrl(getString(R.string.api_root))
            .client(httpClient).build()

        val requstApi = retrofit.create(MyAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = requstApi.postSignIn(binding.emailEdt.text.toString(),
                    binding.userCode.text.toString()).awaitResponse()

                if (response.isSuccessful){
                    val data = response.body()!!

                    shrdPrefManager.JWT = "Bearer " + data

                    Log.d("Gained JWT", data.toString())
                }

                Log.d("Response", "Is ok")

            } catch (e: Exception){
                shrdPrefManager.JWT = " "
                Log.d(ContentValues.TAG, e.toString())
                Log.d("Something went bad", e.toString())
                Log.d("Code of Response", e.message.toString() )
            }
        }
    }


}