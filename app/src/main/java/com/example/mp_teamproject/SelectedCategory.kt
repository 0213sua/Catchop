package com.example.mp_teamproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mp_teamproject.databinding.ActivitySelectedCategoryBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_selected_category.*
import kotlinx.android.synthetic.main.item_main.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SelectedCategory : AppCompatActivity() {
    val binding by lazy { ActivitySelectedCategoryBinding.inflate(layoutInflater)}

    val surveys: MutableList<SurveyData> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val yearformatter = DateTimeFormatter.ofPattern("yyyy")
    @RequiresApi(Build.VERSION_CODES.O)
    val monthformatter = DateTimeFormatter.ofPattern("MM")
    @RequiresApi(Build.VERSION_CODES.O)
    val dayformatter = DateTimeFormatter.ofPattern("dd")

    @RequiresApi(Build.VERSION_CODES.O)
    val today = current.format(formatter)
    @RequiresApi(Build.VERSION_CODES.O)
    val day_of_today = current.format(dayformatter).toInt()
    @RequiresApi(Build.VERSION_CODES.O)
    val month_of_today = current.format(monthformatter).toInt()
    @RequiresApi(Build.VERSION_CODES.O)
    val year_of_today = current.format(yearformatter).toInt()

    val categoryFragment = CategoryFragment()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("ITM","today:"+today)
        Log.d("ITM","day of today:"+day_of_today)
        Log.d("ITM","month of today:"+month_of_today)

        surveys.clear()

        val categoryName = intent.getStringExtra("category_name1")
        Log.d("cate","categoryname : $categoryName")

        if (intent.hasExtra("category_name1")) {
            binding.categoryName.text = intent.getStringExtra("category_name1")
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }

        binding.arrow.setOnClickListener {
            val intent = Intent(this, Main::class.java)
            intent.putExtra("back", "back")
            startActivity(intent)
        }

        binding.floatingBtn2.setOnClickListener {
            val intent = Intent(this, CreateSurvey::class.java)
            startActivity(intent)
        }
        binding.scOnBtn.setOnClickListener{
            Log.d("진행","진행버튼 클릭 대기 중")

            (binding.scRecyclerView.adapter as MyAdapter).onButton(categoryName.toString())
            Log.d("진행","진행버튼 클릭 성공")
        }
        binding.scFinBtn.setOnClickListener{
            Log.d("완료","완료버튼 클릭 대기 중")
            (binding.scRecyclerView.adapter as MyAdapter).finButton(categoryName.toString())
            Log.d("완료","완료 클릭 성공")
        }


        //recycler view에 띄워보자

        // recyclerview에 layoutmanager 설정
        val layoutManager = LinearLayoutManager(this@SelectedCategory)

        // recyclerview의 아이템을 역순으로 정렬
        layoutManager.reverseLayout = true

        // recyclerview의 아이템을 쌓는 순서를 끝부터 쌓게 함
        layoutManager.stackFromEnd = true

        binding.scRecyclerView.layoutManager = layoutManager
        binding.scRecyclerView.adapter = MyAdapter()

        //firebase에서 survey 데이터를 가져온 후 surveys 변수에 저장
        FirebaseDatabase.getInstance().getReference("/Surveys")
            .orderByChild("category").equalTo(categoryName).addChildEventListener(object : ChildEventListener {
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
                                binding.scRecyclerView.adapter?.notifyItemInserted(surveys.size -1)
                            }else{
                                // 글이 중간에 삽입된 경우 prevChildKey로 한단계 앞의 데이터 위치를 찾은 뒤 데이터를 추가한다.
                                val prevIndex = surveys.map {it.surveyId}.indexOf(prevChildKey)
                                surveys.add(prevIndex+1,survey)
                                //recycler view의 adapter에 글이 추가된 것을 알림
                                binding.scRecyclerView.adapter?.notifyItemInserted(prevIndex+1)
                            }
                        }
                    }
                }
                // 설문지가 변경된 경우
                override fun onChildChanged(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot?.let { snapshot ->
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val survey = snapshot.getValue(SurveyData::class.java)
                        survey?.let { survey ->
                            // 글이 변경된 경우 글의 앞의 데이터 인덱스에 데이터를 변경한다.
                            val prevIndex = surveys.map { it.surveyId }.indexOf(prevChildKey)
                            surveys[prevIndex + 1] = survey
                            binding.scRecyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot?.let {
                        // snapshot 의 데이터를 surveyData 객체로 가져옴
                        val survey = snapshot.getValue(SurveyData::class.java)
                        //
                        survey?.let { survey ->
                            // 기존에 저장된 인덱스를 찾아서 해당 인덱스의 데이터를 삭제한다.
                            val existIndex = surveys.map { it.surveyId }.indexOf(survey.surveyId)
                            surveys.removeAt(existIndex)
                            binding.scRecyclerView.adapter?.notifyItemRemoved(existIndex)
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
                            binding.scRecyclerView.adapter?.notifyItemRemoved(existIndex)
                            //prevChildKey가 없는 경우 맨마지막으로 이동 된 것
                            if (prevChildKey == null){
                                surveys.add(survey)
                                binding.scRecyclerView.adapter?.notifyItemChanged(surveys.size-1)
                            }else{
                                //prevChildKey 다음 글로 추가
                                val prevIndex = surveys.map{it.surveyId}.indexOf(prevChildKey)
                                surveys.add(prevIndex + 1, survey)
                                binding.scRecyclerView.adapter?.notifyItemChanged(prevIndex+1)
                            }
                        }
                    }
                }
                // 취소된 경우
                override fun onCancelled(error: DatabaseError) {
                    error?.toException()?.printStackTrace()
                }
            })
    }

    inner class MyViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //설문지 제목 텍스트 뷰
        val titleText : TextView = itemView.titleText
        //설문지 기간 텍스트 뷰
        val dateText : TextView = itemView.dateText
        // 북마크 이미지뷰
        //val bookmarkView : ImageView = itemView.bookmarkView
    }

    // RecyclerView 의 어댑터 클래스
    inner class MyAdapter : RecyclerView.Adapter<MyViewHodler>() {
        // RecyclerView 에서 각 Row(행)에서 그릴 ViewHolder 를 생성할때 불리는 메소드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHodler {
            return MyViewHodler(
                LayoutInflater.from(this@SelectedCategory).inflate(R.layout.item_main,
                    parent, false))
        }

        // RecyclerView 에서 몇개의 행을 그릴지 기준이 되는 메소드
        override fun getItemCount(): Int {
            return surveys.size
        }
        //진행 중 버튼

        @RequiresApi(Build.VERSION_CODES.O)
        fun onButton(categoryName: String) {
            FirebaseDatabase.getInstance().reference.child("/Surveys").orderByChild("category").equalTo(categoryName)
                .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //val value = snapshot.getValue<SurveyData>()
                    Log.d("진행","surveys 초기화")
                    surveys.clear()
                    for(snapshot in snapshot!!.children){
                        val survey : SurveyData? = snapshot.getValue(SurveyData::class.java)
                        val enddate = survey?.endDate.toString().split('.')
                        val e_year = enddate[0].toInt()
                        val e_month = enddate[1].toInt()
                        val e_day = enddate[2].toInt()

                        Log.d("진행","snapshot.title: "+enddate)
                        if(e_year >= year_of_today){
                            if(e_year > year_of_today){
                                surveys.add(survey!!)
                            }else{  //end year과 금년이 같은 경우
                                if(e_month >= month_of_today){
                                    if(e_month > month_of_today){
                                        surveys.add(survey!!)
                                    }else{  // end month와 corrent month가 같은 경우
                                        if(e_day >= day_of_today){
                                            surveys.add(survey!!)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        fun finButton(categoryName: String) {
            FirebaseDatabase.getInstance().reference.child("/Surveys").orderByChild("category").equalTo(categoryName)
                .addValueEventListener(object :ValueEventListener{
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(snapshot: DataSnapshot) {
                        surveys.clear()
                        for(snapshot in snapshot!!.children){
                            val survey : SurveyData? = snapshot.getValue(SurveyData::class.java)
                            val enddate = survey?.endDate.toString().split('.')
                            val e_year = enddate[0].toInt()
                            val e_month = enddate[1].toInt()
                            val e_day = enddate[2].toInt()

                            Log.d("마감","snapshot.title: "+enddate)
                            if(e_year <= year_of_today){
                                if(e_year < year_of_today){
                                    surveys.add(survey!!)
                                }else{  //end year과 금년이 같은 경우
                                    if(e_month <= month_of_today){
                                        if(e_month < month_of_today){
                                            surveys.add(survey!!)
                                        }else{  // end month와 corrent month가 같은 경우
                                            if(e_day < day_of_today){
                                                surveys.add(survey!!)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        notifyDataSetChanged()
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }

        // 각 행의 포지션에서 그려야할 ViewHolder UI에 데이터를 적용하는 메소드
        override fun onBindViewHolder(holder: MyViewHodler, position: Int) {
            val survey = surveys[position]
            // 카드에 글을 세팅
            holder.titleText.text = survey.title
            // 글이 쓰여진 시간
            holder.dateText.text = survey.startDate+" ~ "+survey.endDate

            // 카드가 클릭되는 경우 DetailActivity 를 실행한다.
            holder.itemView.setOnClickListener {
                // 상세화면을 호출할 Intent 를 생성한다.
                val intent = Intent(this@SelectedCategory, SurveyInfo::class.java)
                // 선택된 카드의 ID 정보를 intent 에 추가한다.
                intent.putExtra("surveyId", survey.surveyId)
                // intent 로 상세화면을 시작한다.
                startActivity(intent)
            }
        }
    }
}