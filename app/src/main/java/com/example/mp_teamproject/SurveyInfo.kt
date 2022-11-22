package com.example.mp_teamproject

import android.content.Intent
import android.net.Uri
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
    var partiUri =""
    var staticUri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //participate btn

        //statistic btn

        val surveyId = intent.getStringExtra("surveyId")
        Log.d("ITM","1")

        binding.siImg2.setOnClickListener {
            val intent = Intent(this,Main::class.java)
            startActivity(intent)
        }


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
                            partiUri = survey.uri
                            var strList: List<String> = survey.uri.split("/")
                            Log.d("ITM","strList : $strList")
                            staticUri = "https://docs.google.com/forms/d/e/"+strList[6]+"/viewanalytics"
                            Log.d("ITM","$staticUri")
                        }
                    }
                }
            })
        //participate btn
        binding.siPartiBtn.setOnClickListener {
            // save implict intent(ACTION_VIEW) & pass uri string(github address)
            val parti = Intent(Intent.ACTION_VIEW, Uri.parse("$partiUri"))
            startActivity(parti)
        }

        //statistic btn

        binding.siStaticBtn.setOnClickListener {
            // save implict intent(ACTION_VIEW) & pass uri string(github address)
            val static = Intent(Intent.ACTION_VIEW, Uri.parse("$staticUri"))
            startActivity(static)
        }
    }
}