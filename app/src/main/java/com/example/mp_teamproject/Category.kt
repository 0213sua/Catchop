package com.example.mp_teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mp_teamproject.databinding.ActivityCategoryBinding

class Category : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("ITM","this is cateogry activity")
    }
}