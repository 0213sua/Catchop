package com.example.mp_teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mp_teamproject.databinding.ActivitySelectedCategoryBinding

class SelectedCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySelectedCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var key = intent.hasExtra("category_name1")
        if (intent.hasExtra("category_name1")) {
            binding.categoryName.text = intent.getStringExtra("category_name1")
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }
        
    }
}