package com.example.mp_teamproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mp_teamproject.databinding.ActivityCreateSurveyBinding
import com.example.mp_teamproject.databinding.ActivitySurveyInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create_survey.*
import kotlinx.android.synthetic.main.activity_survey_info.*
import kotlinx.android.synthetic.main.activity_survey_list_test.*
import kotlinx.android.synthetic.main.survey_post.view.*

class SurveyInfo : AppCompatActivity() {
    val binding by lazy{ ActivitySurveyInfoBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //participate btn

        //statistic btn

        val surveyId = intent.getStringExtra("surveyId")
        Log.d("ITM","1")

        FirebaseDatabase.getInstance().getReference("/Surveys/$surveyId")
            .addValueEventListener(object:ValueEventListener{

                override fun onCancelled(error: DatabaseError) {
                }
                // data 읽기
                /*
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val survey = dataSnapshot.getValue(SurveyData::class.java)
                    if (survey != null) {
                        si_titleText.setText(survey.title)
                    }
                    if (survey != null) {
                        si_instText.setText(survey.institution)
                    }
                    if (survey != null) {
                        si_sdateText.setText(survey.startDate)
                    }
                    if (survey != null) {
                        si_edateText.setText(survey.endDate)
                    }
                    if (survey != null) {
                        si_purposeText.setText(survey.purpose)
                    }
                    if (survey != null) {
                        si_contentText.setText(survey.surveyContent)
                    }
                }

                 */


                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("ITM","2")
                    snapshot?.let{
                        val survey = it.getValue(SurveyData::class.java)
                        Log.d("ITM","3")
                        survey?.let{
                            Log.d("ITM","4")

                            binding.siTitleText.text = survey.title
                            binding.siInstText.text = survey.institution
                            binding.siSdateText.text = survey.startDate
                            binding.siEdateText.text = survey.endDate
                            binding.siPurposeText.text = survey.purpose
                            binding.siContentText.text = survey.surveyContent
                            Log.d("ITM","5")

                        }
                    }
                }
            })
    }
}