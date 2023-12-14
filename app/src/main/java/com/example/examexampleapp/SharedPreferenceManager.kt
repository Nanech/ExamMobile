package com.example.examexampleapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharedPreferenceManager (context: Context) {

    private val preferences = context.getSharedPreferences(
        "ExampleApp", AppCompatActivity.MODE_PRIVATE
    )

    private val editor = preferences.edit()


    var JWT
        get() = preferences.getString("JWT", null)
        set(value) {
            editor.putString("JWT", value)
            editor.apply()
        }


}