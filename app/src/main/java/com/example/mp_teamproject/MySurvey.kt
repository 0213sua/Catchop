package com.example.mp_teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MySurvey : AppCompatActivity() {
    //val binding by lazy { ActivityMySurveyBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_survey)
    }
}