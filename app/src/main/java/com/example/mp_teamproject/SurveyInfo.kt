package com.example.mp_teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp_teamproject.databinding.ActivityCreateSurveyBinding
import kotlinx.android.synthetic.main.activity_survey_list_test.*

class SurveyInfo : AppCompatActivity() {
    val binding by lazy{ ActivityCreateSurveyBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val surveyID = intent.getStringExtra("surveyID")

        val layoutManager = LinearLayoutManager(this@SurveyInfo)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL //?
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter
    }
}