package com.example.examexampleapp.api

import com.example.examexampleapp.models.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MyAPI {


    @POST("api/SendCode")
    fun postSendEmail(@Header("User-email") email: String ) : Call<String>

    @POST("api/SignIn")
    fun postSignIn( @Header("User-email") email: String,
        @Header("User-code") code: String) : Call<String>

    @GET("api/News")
    fun getNews() : Call<List<News>>


}