package com.example.mp_teamproject

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.mp_teamproject.databinding.ActivitySurveyInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create_survey.*
import kotlinx.android.synthetic.main.activity_survey_info.*
import kotlinx.android.synthetic.main.activity_survey_list_test.*
import kotlinx.android.synthetic.main.survey_post.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SurveyInfo : AppCompatActivity() {
    val binding by lazy{ ActivitySurveyInfoBinding.inflate(layoutInflater)}
    private var auth : FirebaseAuth? = null
    var partiUri =""
    var staticUri = ""
    val surveys: MutableList<SurveyData> = mutableListOf()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("/Surveys")
    private var surveyId = " "
    private var enddate = ""
    private var writer = ""


    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val today = current.format(formatter)



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.arrow.setOnClickListener {
            val intent = Intent(this,Main::class.java)
            startActivity(intent)
        }

        surveyId = intent.getStringExtra("surveyId").toString()

        auth = FirebaseAuth.getInstance()
        val userid = auth!!.currentUser?.uid

        FirebaseDatabase.getInstance().getReference("/Surveys/$surveyId")
            .addValueEventListener(object:ValueEventListener{

                override fun onCancelled(error: DatabaseError) {
                }
                // data 읽기
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot?.let{
                        val survey = it.getValue(SurveyData::class.java)

                        survey?.let{
                            binding.siTitleText.text = survey.title
                            binding.siInstText.text = survey.institution
                            binding.siSdateText.text = survey.startDate
                            binding.siEdateText.text = survey.endDate
                            binding.siPurposeText.text = survey.purpose
                            binding.siContentText.text = survey.surveyContent
                            binding.siCateText.text = survey.category
                            binding.siResultText.text = survey.resultOpen

                            enddate = survey.endDate
                            writer = survey.writerId
                            Log.d("aa","1 writer id : $writer")
                            Log.d("aa","1 end date : $enddate")

                            partiUri = survey.uri
                            var strList: List<String> = survey.uri.split("/")
                            staticUri = "https://docs.google.com/forms/d/e/"+strList[6]+"/viewanalytics"
                        }
                    }
                    Log.d("aa","2 writer id : $writer")
                    Log.d("aa","2 end date : $enddate")
                    Log.d("aa","2 userid : $userid")
                    if (writer == userid){
                        binding.siDeleteBtn.visibility = View.VISIBLE
            //            View.VISIBLE, View.INVISIBLE, View.GONE
                    } else{
                        binding.siDeleteBtn.visibility = View.GONE
                    }
                    Log.d("aa","today : $today, enddate : $enddate, today>enddate : ${today>enddate}")
                    if (today>enddate){
                        binding.siPartiBtn.isEnabled = false //비활성화
                        binding.siStaticBtn.isEnabled = true //활성화
                    } else{
                        binding.siPartiBtn.isEnabled = true //활성화
                        binding.siStaticBtn.isEnabled = false //비활성화
                    }

                    }
            })

//        값이 안받아와짐
        Log.d("aa","wirter ID : $writer, userid : $userid")
        Log.d("aa","today : $today, endDate : $enddate")

//        if (writer == userid){
//            binding.siDeleteBtn.visibility = View.VISIBLE
////            View.VISIBLE, View.INVISIBLE, View.GONE
//        } else{
//            binding.siDeleteBtn.visibility = View.GONE
//        }
//        if (today>enddate){
//            binding.siPartiBtn.isEnabled = false //비활성화
//            binding.siStaticBtn.isEnabled = true //활성화
//        } else{
//            binding.siPartiBtn.isEnabled = true //활성화
//            binding.siStaticBtn.isEnabled = false //비활성화
//        }




        //participate btn
        binding.siPartiBtn.setOnClickListener {
//            databaseReference.child(surveyId).child("surveyorInfo").setValue(userid)
            databaseReference.child(surveyId).child("surveyorInfo").setValue(userid)
            // save implict intent(ACTION_VIEW) & pass uri string(github address)
            Log.d("aa","partiUri : $partiUri")
            val parti = Intent(Intent.ACTION_VIEW, Uri.parse("$partiUri"))
            startActivity(parti)
        }

        //statistic btn
        binding.siStaticBtn.setOnClickListener {
            // save implict intent(ACTION_VIEW) & pass uri string(github address)
            Log.d("aa","staticUri : $staticUri")
            val static = Intent(Intent.ACTION_VIEW, Uri.parse("$staticUri"))
            startActivity(static)
        }
    }
}