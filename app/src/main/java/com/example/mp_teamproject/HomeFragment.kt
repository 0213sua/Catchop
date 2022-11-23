package com.example.mp_teamproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mp_teamproject.databinding.FragmentCategoryBinding
import com.example.mp_teamproject.databinding.FragmentHomeBinding
import com.example.mp_teamproject.databinding.ItemMainBinding
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {
    companion object {
        fun newInstance():HomeFragment{
            return HomeFragment()
        }
    }
    private lateinit var database: DatabaseReference
    val surveys : MutableList<SurveyData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.floatingBtn1.setOnClickListener {
            val intent = Intent(this@HomeFragment.requireContext(),CreateSurvey::class.java)
            startActivity(intent)
        }


        //리사이클러 뷰에 LayoutManager, Adapter, (ItemDecoration 적용)
        val layoutManager = LinearLayoutManager(activity)
        // recyclerview의 아이템을 역순으로 정렬
        layoutManager.reverseLayout = true
        // recyclerview의 아이템을 쌓는 순서를 끝부터 쌓게 함
        layoutManager.stackFromEnd = true

        binding.recyclerView1.layoutManager = layoutManager

        binding.finBtn.setOnClickListener{
            surveys.clear()
            binding.recyclerView1.adapter = finAdapter(surveys)

        }

        binding.onBtn.setOnClickListener{
            surveys.clear()
            binding.recyclerView1.adapter = onAdapter(surveys)
        }

        surveys.clear()
        binding.recyclerView1.adapter = onAdapter(surveys)


        return binding.root

    }

    // RecyclerView 의 어댑터 클래스
    @RequiresApi(Build.VERSION_CODES.O)
    // 저장된 endDate의 값보다 현재날짜가 작거나 같을때
    inner class onAdapter(val surveys: MutableList<SurveyData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        init{
            //firebase에서 survey 데이터를 가져온 후 surveys 변수에 저장
            FirebaseDatabase.getInstance().getReference("/Surveys")
                .orderByChild("endDate").startAfter(today).addChildEventListener(object : ChildEventListener {
                    //설문이 추가된 경우
                    override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                        snapshot?.let{snapshot->
                            //snapshot의 데이터를 survey 객체로 가져옴
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let{
                                //새 글이 마지막 부분에 추가된 경우
                                if (prevChildKey == null){
                                    //글 목록을 저장하는 변수에 post 객체 추가
                                    surveys.add(it)
                                    // recyclerview의 adapter에 글이 추가된 것을 알림
                                    //binding.recyclerView1.adapter?.notifyItemInserted(surveys.size -1)
                                    notifyItemInserted(surveys.size -1)
                                }else{
                                    // 글이 중간에 삽입된 경우 prevChildKey로 한단계 앞의 데이터 위치를 찾은 뒤 데이터를 추가한다.
                                    val prevIndex = surveys.map {it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex+1,survey)
                                    //recycler view의 adapter에 글이 추가된 것을 알림
                                    notifyItemInserted(prevIndex+1)
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
                                notifyItemChanged(prevIndex + 1) //체크
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
                                notifyItemRemoved(existIndex) //체크
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
                                notifyItemRemoved(existIndex)
                                //prevChildKey가 없는 경우 맨마지막으로 이동 된 것
                                if (prevChildKey == null){
                                    surveys.add(survey)
                                    notifyItemChanged(surveys.size-1)
                                }else{
                                    //prevChildKey 다음 글로 추가
                                    val prevIndex = surveys.map{it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex + 1, survey)
                                    notifyItemChanged(prevIndex+1)
                                }
                            }
                        }
                    }
                    // 취소된 경우
                    override fun onCancelled(error: DatabaseError) {
                        //error?.toException()?.printStackTrace()
                    }
                })

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(context),parent, false))
        }

        // RecyclerView 에서 몇개의 행을 그릴지 기준이 되는 메소드
        override fun getItemCount(): Int {
            return surveys.size
        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val binding = (holder as MyViewHolder).binding
            val survey = surveys[position]
            // 카드에 글을 세팅
            binding.titleText.text = survey.title
            // 글이 쓰여진 시간
            binding.dateText.text = survey.startDate+" ~ "+survey.endDate

            // 카드가 클릭되는 경우 DetailActivity 를 실행한다.
            binding.itemRoot.setOnClickListener {
                // 상세화면을 호출할 Intent 를 생성한다.
                val intent = Intent(context, SurveyInfo::class.java)
                // 선택된 카드의 ID 정보를 intent 에 추가한다.
                intent.putExtra("surveyId", survey.surveyId)
                // intent 로 상세화면을 시작한다.
                startActivity(intent)
            }
        }

        //리사이클러 뷰에 LayoutManager, Adapter, (ItemDecoration 적용)
        val layoutManager = LinearLayoutManager(activity)
        // recyclerview의 아이템을 역순으로 정렬
        //layoutManager.reverseLayout = true
        // recyclerview의 아이템을 쌓는 순서를 끝부터 쌓게 함
        //layoutManager.stackFromEnd = true
        binding.recyclerView1.layoutManager = layoutManager
        binding.recyclerView1.adapter = MyAdapter(surveys)

        return binding.root

    }

    // RecyclerView 의 어댑터 클래스
    inner class MyAdapter(val surveys: MutableList<SurveyData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        init{
            //firebase에서 survey 데이터를 가져온 후 surveys 변수에 저장
            FirebaseDatabase.getInstance().getReference("/Surveys")
                .orderByChild("startDate").addChildEventListener(object : ChildEventListener {
                    //설문이 추가된 경우
                    override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                        snapshot?.let{snapshot->
                            //snapshot의 데이터를 survey 객체로 가져옴
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let{
                                //새 글이 마지막 부분에 추가된 경우
                                if (prevChildKey == null){
                                    //글 목록을 저장하는 변수에 post 객체 추가
                                    surveys.add(it)
                                    // recyclerview의 adapter에 글이 추가된 것을 알림
                                    //binding.recyclerView1.adapter?.notifyItemInserted(surveys.size -1)
                                    notifyItemInserted(surveys.size -1)
                                }else{
                                    // 글이 중간에 삽입된 경우 prevChildKey로 한단계 앞의 데이터 위치를 찾은 뒤 데이터를 추가한다.
                                    val prevIndex = surveys.map {it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex+1,survey)
                                    //recycler view의 adapter에 글이 추가된 것을 알림
                                    notifyItemInserted(prevIndex+1)
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
                                notifyItemChanged(prevIndex + 1) //체크
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
                                notifyItemRemoved(existIndex) //체크
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
                                notifyItemRemoved(existIndex)
                                //prevChildKey가 없는 경우 맨마지막으로 이동 된 것
                                if (prevChildKey == null){
                                    surveys.add(survey)
                                    notifyItemChanged(surveys.size-1)
                                }else{
                                    //prevChildKey 다음 글로 추가
                                    val prevIndex = surveys.map{it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex + 1, survey)
                                    notifyItemChanged(prevIndex+1)
                                }
                            }
                        }
                    }
                    // 취소된 경우
                    override fun onCancelled(error: DatabaseError) {
                        //error?.toException()?.printStackTrace()
                    }
                })

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(context),parent, false))
        }

        // RecyclerView 에서 몇개의 행을 그릴지 기준이 되는 메소드
        override fun getItemCount(): Int {
            return surveys.size
        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val binding = (holder as MyViewHolder).binding
            val survey = surveys[position]
            // 카드에 글을 세팅
            binding.titleText.text = survey.title
            // 글이 쓰여진 시간
            binding.dateText.text = survey.startDate+" ~ "+survey.endDate

            // 카드가 클릭되는 경우 DetailActivity 를 실행한다.
            binding.itemRoot.setOnClickListener {
                // 상세화면을 호출할 Intent 를 생성한다.
                val intent = Intent(context, SurveyInfo::class.java)
                // 선택된 카드의 ID 정보를 intent 에 추가한다.
                intent.putExtra("surveyId", survey.surveyId)
                // intent 로 상세화면을 시작한다.
                startActivity(intent)
            }
        }
        inner class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root){
            //설문지 제목 텍스트 뷰
            val titleText : TextView = binding.titleText
            //설문지 기간 텍스트 뷰
            val dateText : TextView = binding.dateText
            // 북마크 이미지뷰
            //val bookmarkView : ImageView = itemView.bookmarkView
        }
        // 각 행의 포지션에서 그려야할 ViewHolder UI에 데이터를 적용하는 메소드


    }



}