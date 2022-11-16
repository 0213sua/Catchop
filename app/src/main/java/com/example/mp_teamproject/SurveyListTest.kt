package com.example.mp_teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp_teamproject.databinding.ActivitySelectedCategoryBinding
import com.example.mp_teamproject.databinding.ActivitySurveyListTestBinding
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ChildEventListener

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

        //firebase에서 survey 데이터를 가져온 후 surveys 변수에 저장
        FirebaseDatabase.getInstance().getReference("/Surveys").orderByChild("startDate").addChildEventListener(object :
            ChildEventListener {
            //설문이 추가된 경우
            override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                snapshot?.let {snapshot->
                    //snapshot의 데이터를 survey 객체로 가져옴
                    val survey = snapshot.getValue(SurveyData::class.java)
                    survey?.let{
                        //새 글이 마지막 부분에 추가된 경우
                        if (prevChildKey == null){
                            //글 목록을 저장하는 변수에 post 객체 추가
                            surveys.add(it)
                            // recyclerview의 adapter에 글이 추가된 것을 알림
                            binding.recyclerView.adapter?.notifyItemInserted(surveys.size -1)
                        }else{
                            // 글이 중간에 삽입된 경우 prevChildKey로 한단계 앞의 데이터 위치를 찾은 뒤 데이터를 추가한다.
                            val prevIndex = surveys.map {it.surveyId}.indexOf(prevChildKey)
                            surveys.add(prevIndex+1,survey)
                            //recycler view의 adapter에 글이 추가된 것을 알림
                            binding.recyclerView.adapter?.notifyItemInserted(prevIndex+1)
                        }
                    }
                }
            }
            // 설문지의 순서가 이동한 경우
            override fun onChildMoved(snapshot: DataSnapshot, prevChildKey: String?) {
                // snapshot
                snapshot?.let{
                    //snapshot의 데이터를 survey 객체로 가져옴
                    val survey = snapshot.getValue(SurveyData::class.java)
                    survey?.let{survey->
                        //기존의 인덱스를 구한다
                        val existIndex = surveys.map{it.surveyId}.indexOf(survey.surveyId)
                        //기존의 데이터를 지운다
                        surveys.removeAt(existIndex)
                        binding.recyclerView.adapter?.notifyItemRemoved(existIndex)
                        //prevChildKey가 없는 경우 맨마지막으로 이동 된 것
                        if (prevChildKey == null){
                            surveys.add(survey)
                            binding.recyclerView.adapter?.notifyItemChanged(surveys.size-1)
                        }else{
                            //prevChildKey 다음 글로 추가
                            val prevIndex = surveys.map{it.surveyId}.indexOf(prevChildKey)
                            surveys.add(prevIndex + 1, survey)
                            binding.recyclerView.adapter?.notifyItemChanged(prevIndex+1)
                        }
                    }
                }
            }

            //설문지가 삭제된 경우 (x)
            //설문지 작성이 취소된 경우
            override fun onCancelled(error: DatabaseError) {
                //취소가된 경우 에러 로그 보여줌
                error?.toException()?.printStackTrace()
            }
        })
    }
    // recycler view에서 사용하는 view 홀더 클래스
    inner class MyViewHolder()
}