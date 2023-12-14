package com.example.examexampleapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examexampleapp.MainActivity
import com.example.examexampleapp.R
import com.example.examexampleapp.adapter.NewsAdapter
import com.example.examexampleapp.api.MyAPI
import com.example.examexampleapp.databinding.ActivityLogicGetBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LogicGetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogicGetBinding

    private lateinit var myAdapter: NewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logic_get)

        binding = ActivityLogicGetBinding.inflate(layoutInflater)
        val view = binding.root

        // Инициализация
        myAdapter = NewsAdapter(this)
        // Настройка адаптера
        binding.newsContainer.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)
        // Присвоение адаптера
        binding.newsContainer.adapter = myAdapter

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor).build()

        val gsonBuilder = GsonBuilder()
            .setLenient().create()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .baseUrl(getString(R.string.api_root))
            .client(client).build()

        val mutex = Mutex()

        val requstApi = retrofit.create(MyAPI::class.java)
        CoroutineScope(Dispatchers.IO).launch {

            mutex.withLock {
                try {
                    val list = requstApi.getNews()
                    val response = list.awaitResponse()

                    if (response.isSuccessful){
                        val news = response.body()!!
                        runOnUiThread {
                            binding.apply {
                                myAdapter.submitList(news)
                            }
                        }
                        Log.d("All right", news.toString())
                    }

                } catch (e: Exception){
                    Log.d("Response Is Failed", e.message.toString())
                }
            }
        }

        binding.btnBack.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
            finish()
        }



        setContentView(view)
    }



}