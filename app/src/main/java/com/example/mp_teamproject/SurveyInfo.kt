package com.example.mp_teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mp_teamproject.databinding.ActivityCreateSurveyBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create_survey.*
import kotlinx.android.synthetic.main.activity_survey_info.*
import kotlinx.android.synthetic.main.activity_survey_list_test.*
import kotlinx.android.synthetic.main.survey_post.view.*

class SurveyInfo : AppCompatActivity() {
    val binding by lazy{ ActivityCreateSurveyBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //participate btn

        //statistic btn

        val surveyId = intent.getStringExtra("surveyId")

        FirebaseDatabase.getInstance().getReference("/Surveys/$surveyId")
            .addValueEventListener(object:ValueEventListener{

                override fun onCancelled(error: DatabaseError) {
                    error?.toException()?.printStackTrace()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot?.let{
                        val survey = it.getValue(SurveyData::class.java)
                        survey?.let{
                            si_titleText.setText(survey.title)
                            si_instText.setText(survey.institution)
                            si_sdateText.setText(survey.startDate)
                            si_edateText.setText(survey.endDate)
                            si_purposeText.setText(survey.purpose)
                            si_contentText.setText(survey.surveyContent)
                        }
                    }
                }
            })
    }
}