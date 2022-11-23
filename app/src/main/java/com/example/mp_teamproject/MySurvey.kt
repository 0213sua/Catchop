package com.example.mp_teamproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mp_teamproject.databinding.ActivityMySurveyBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth


class MySurvey : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    val binding by lazy { ActivityMySurveyBinding.inflate(layoutInflater)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        //유저 아이디 가져옴
        val userid = auth!!.currentUser?.uid


    }
}