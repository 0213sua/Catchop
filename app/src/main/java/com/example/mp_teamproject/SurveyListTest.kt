package com.example.mp_teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mp_teamproject.databinding.ActivitySelectedCategoryBinding
import com.example.mp_teamproject.databinding.ActivitySurveyListTestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class SurveyListTest : AppCompatActivity() {
    val binding by lazy { ActivitySurveyListTestBinding.inflate(layoutInflater)}

    //로그에 tag로 사용할 문자열
    val TAG = "SurveyListTest"

    //파이어베이스의 test key를 가진 데이터의 참조 객체를 가져온다
    val ref = FirebaseDatabase.getInstance().getReference("test")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //값의 변경이 있는 경우의 이벤트 리스너 추가
        ref.addValueEventListener(object:ValueEventListener{
            // 데이터 읽기가 취소된 경우 호출된다
            // 데이터 권한이 없는 경우
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }

            //데이터 변경이 감지되면 호출된다
            override fun onDataChange(snapshot: DataSnapshot) {
                //test key를 가진 데이터 스냅샷에서 값을 읽고 문자열로 변경
                val message = snapshot.value.toString()
                //읽은 문자열 로깅
                Log.d(TAG,message)
                // 파이어베이스에서 전달받은 메시지로 제목을 변경
                supportActionBar?.title = message
            }
        })



    }
}