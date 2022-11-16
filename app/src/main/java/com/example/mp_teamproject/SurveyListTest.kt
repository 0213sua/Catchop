package com.example.mp_teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp_teamproject.databinding.ActivitySelectedCategoryBinding
import com.example.mp_teamproject.databinding.ActivitySurveyListTestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class SurveyListTest : AppCompatActivity() {
    val binding by lazy { ActivitySurveyListTestBinding.inflate(layoutInflater)}

    val surveys: MutableList<SurveyData> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.floatingBtn.setOnClickListener {
            //save explicit intent
            val create = Intent(this, CreateSurvey::class.java)
            startActivity(create)
        }

        // recyclerview에 layoutmanager 설정
        val layoutManager = LinearLayoutManager(this@SurveyListTest)

        // recyclerview의 아이템을 역순으로 정렬
        layoutManager.reverseLayout = true

        // recyclerview의 아이템을 쌓는 순서를 끝부터 쌓게 함
        layoutManager.stackFromEnd = true

        binding.recyclerView.layoutManager = layoutManager //binding 추가
        binding.recyclerView.adapter = MyAdapter()






    }
}