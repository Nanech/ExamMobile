package com.example.examexampleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.examexampleapp.activity.LogicGetActivity
import com.example.examexampleapp.activity.LogicPostActivity
import com.example.examexampleapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root



        // Button Post Event
        binding.btnPost.setOnClickListener {
            val postIntent = Intent(this, LogicPostActivity::class.java)
            startActivity(postIntent)
        }

        binding.btnGet.setOnClickListener {
            val getIntent = Intent(this, LogicGetActivity::class.java)
            startActivity(getIntent)
        }


        setContentView(view) // Set MainActivity view
    }
}