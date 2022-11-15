package com.example.mp_teamproject

import android.R
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp_teamproject.databinding.ActivityCreateSurveyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

//import com.google.firebase.ktx.Firebase


class CreateSurvey : AppCompatActivity() {
    val binding by lazy{ ActivityCreateSurveyBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // surveyData 객체 생성
        val survey = SurveyData()
        // firebase의 Surveys 참조에서 객체를 저장하기 위한 새로운 키를 생성하고 참조를 newRef에 저장
        val newRef = FirebaseDatabase.getInstance().getReference("Surveys").push()

        // spinner
        val myList = listOf("","Administration","Technology","Education","Traffic","Land managing","Agriculture","Culture","Health","Food")

        //provide views for an AdapterView
        val myAdapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, myList)

        // connect myAdapter to the Spinner
        binding.categorySpinner.adapter = myAdapter
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2==0){
                    survey.category = ""
                }
                if(p2==1){
                    survey.category = "Administration"
                }
                if(p2==2){
                    survey.category = "Technology"
                }
                if(p2==3){
                    survey.category = "Education"
                }
                if(p2==4){
                    survey.category = "Traffic"
                }
                if(p2==5){
                    survey.category = "Land Managing"
                }
                if(p2==6){
                    survey.category = "Agriculture/Livestock/Fish"
                }
                if(p2==7){
                    survey.category = "Culture"
                }
                if(p2==8){
                    survey.category = "Health/Medical"
                }
                if(p2==9){
                    survey.category = "Food"
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        //// registerBtn 클릭시, 작성된 survey 정보를 firebase에 저장 (+ 작성자 ID)

        // registerBtn 클릭된 경우, 이벤트 리스너 설정
        binding.registerBtn.setOnClickListener {
            //빈칸을 다 채우지 않았을 경우 토스트 메시지로 알림.
            if(TextUtils.isEmpty(binding.titleText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(binding.writerText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(binding.InstitutionText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(binding.purposeText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(binding.startDate.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(binding.endDate.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(binding.contentText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(binding.uriText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 설문지의 id는 새로 생성된 파이어베이스 참조의 키로 할당
            survey.surveyId = newRef.key.toString() //newRef.key가 아니라..?
            // 로그인 정보에서 email 정보 할당
            var auth : FirebaseAuth? = null
            auth = Firebase.auth
            survey.writerId = auth?.currentUser?.uid.toString() // 이것도 맨 마지막에 toString()추가함..

//            survey.participantId //이건 여기서 할당 못함. participate 페이지에서 할당
            // post 객체에 input EditText의 텍스트 내용을 할당
            survey.title = binding.titleText.text.toString()
            survey.writer = binding.writerText.text.toString()
            survey.institution = binding.InstitutionText.text.toString()
            survey.purpose = binding.purposeText.text.toString()

            survey.startDate = binding.startDate.text.toString()
            survey.endDate = binding.endDate.text.toString()
            survey.surveyorInfo //아니 이건.. 어케 받아야함? checkbox정보는 어떻게 받아오는거지?
            survey.surveyContent = binding.contentText.text.toString()
            survey.uri = binding.uriText.text.toString()
            // surveyData 객체를 새로 생성한 참조에 저장
            newRef.setValue(survey)
            //저장 성공 토스트 알림을 보여주고 activity 종료 -> surveyList화면으로 전환이 더 나은가?
            Toast.makeText(applicationContext,"Created your survey", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}