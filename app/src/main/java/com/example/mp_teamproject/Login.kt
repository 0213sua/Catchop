package com.example.mp_teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mp_teamproject.databinding.ActivityLoginBinding
import com.google.firebase.ktx.Firebase



class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LoginBtn.setOnClickListener{
            val intent = Intent(this,Main::class.java)
            startActivity(intent)
        }

        binding.LISignUpBtn.setOnClickListener{
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }



    }
}