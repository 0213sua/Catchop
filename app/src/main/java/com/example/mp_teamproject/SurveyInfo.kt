package com.example.mp_teamproject

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SurveyInfo : AppCompatActivity() {
    val binding by lazy{ ActivitySurveyInfoBinding.inflate(layoutInflater)}
    private var auth : FirebaseAuth? = null
    var partiUri =""
    var staticUri = ""
    var stOpen = ""
    val surveys: MutableList<SurveyData> = mutableListOf()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("/Surveys")

    private var enddate = ""
    private var surveyId = " "
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
//            val intent = Intent(this,Main::class.java)
//            startActivity(intent)
            finish()
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

                    snapshot?.let {
                        val survey = it.getValue(SurveyData::class.java)

                        survey?.let {
                            binding.siTitleText.text = survey.title
                            binding.siInstText.text = survey.institution
                            binding.siSdateText.text = survey.startDate

                            binding.siEdateText.text = survey.endDate

                            binding.siPurposeText.text = survey.purpose
                            binding.siContentText.text = survey.surveyContent
                            binding.siCateText.text = survey.category
                            binding.siResultText.text = survey.resultOpen


                            writer = survey.writerId
                            partiUri = survey.uri
                            var strList: List<String> = survey.uri.split("/")
                            staticUri =
                                "https://docs.google.com/forms/d/e/" + strList[6] + "/viewanalytics"
                        }
                    }
                    // delete 버튼 활성화, 비활성화
                    if (writer == userid) {
                        binding.siDeleteBtn.visibility = View.VISIBLE
                    } else {
                        binding.siDeleteBtn.visibility = View.GONE
                    }
                }
            })

        FirebaseDatabase.getInstance().getReference("/Surveys/$surveyId/endDate")
            .addValueEventListener(object:ValueEventListener{

                override fun onCancelled(error: DatabaseError) {
                }
                // data 읽기
                override fun onDataChange(snapshot: DataSnapshot) {
                    enddate = snapshot.getValue() as String
                    Log.d("ITM", "Value is: $enddate")
                }
            })

        FirebaseDatabase.getInstance().getReference("/Surveys/$surveyId/resultOpen")
            .addValueEventListener(object:ValueEventListener{

                override fun onCancelled(error: DatabaseError) {
                }
                // data 읽기
                override fun onDataChange(snapshot: DataSnapshot) {
                    stOpen = snapshot.getValue() as String
                    Log.d("ITM", "stopen: $stOpen")
                }
            })

        binding.siPartiBtn.setOnClickListener {

            firebaseDatabase.getReference("/Surveys").child(surveyId).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot?.let { snapshot ->
                        val survey = snapshot.getValue(SurveyData::class.java)
                        val origin = survey?.surveyorInfo
                        Log.d("ITM","현재 저장되어 있는 값"+origin)


                        if(!(survey!!.surveyorInfo.contains(userid.toString()))){
                            val change = origin + "," + userid.toString()
                            Log.d("ITM","다시 설정한 값"+change)
                            snapshot.child("surveyorInfo").ref.setValue(change)

                        }

                    }

                }

            })


            Log.d("aa","today : $today, enddate : $enddate, today>enddate : ${today>enddate}")
            databaseReference.child(surveyId).child("surveyorInfo").setValue(userid)
            // save implict intent(ACTION_VIEW) & pass uri string(github address)
            Log.d("aa","partiUri : $partiUri")
            if(today>enddate){
                Toast.makeText(applicationContext, "Survey is ended :(", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                val parti = Intent(Intent.ACTION_VIEW, Uri.parse("$partiUri"))
                startActivity(parti)
            }
        }
        binding.siDeleteBtn.setOnClickListener{
            val dataRef = firebaseDatabase.getReference("/Surveys/$surveyId")
            Log.d("ITM","dataRef: $dataRef")
            dataRef.removeValue()
            Toast.makeText(applicationContext, "Survey is deleted!", Toast.LENGTH_LONG).show()
        }
        //statistic btn
        binding.siStaticBtn.setOnClickListener {
            Log.d("aa","today : $today, enddate : $enddate, today<enddate : ${today<enddate}")
            // save implict intent(ACTION_VIEW) & pass uri string(github address)
            Log.d("aa","staticUri : $staticUri")
            if(today<=enddate || stOpen=="NO"){
                Toast.makeText(applicationContext, "Statistics is not opened :(", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                val static = Intent(Intent.ACTION_VIEW, Uri.parse("$staticUri"))
                startActivity(static)
            }
        }
    }
}