package com.example.mp_teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mp_teamproject.databinding.ActivitySelectedCategoryBinding

class SelectedCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySelectedCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}