package com.example.mp_teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mp_teamproject.databinding.ActivityMyTreeBinding

class MyTree : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMyTreeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}