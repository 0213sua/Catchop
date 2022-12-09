package com.example.mp_teamproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mp_teamproject.databinding.FragmentHomeBinding
import com.example.mp_teamproject.databinding.ItemMainBinding
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment(R.layout.fragment_home) {

    val surveys : MutableList<SurveyData> = mutableListOf()
    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val today = current.format(formatter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("home","today : $today")
        val binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.floatingBtn1.setOnClickListener {
            val intent = Intent(this@HomeFragment.requireContext(),CreateSurvey::class.java)
            startActivity(intent)
        }


        //Set LayoutManager, Adapter in the linearlayout(ItemDecoration 적용)
        val layoutManager = LinearLayoutManager(activity)
        // Sort item of recyclerview in reverse order
        layoutManager.reverseLayout = true
        // Stack the item of recyclerview from the end
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
        binding.homeSearchImg.setOnClickListener {
            Log.d("MP","text: "+binding.editSearch.text)
            surveys.clear()
            binding.recyclerView1.adapter = SearchAdapter(surveys,binding.editSearch.text.toString())
            //(binding.recyclerView1.adapter as onAdapter).search(binding.editSearch.text.toString())
        }

        surveys.clear()
        binding.recyclerView1.adapter = MyAdapter(surveys)

        return binding.root
    }

    inner class SearchAdapter(val surveys: MutableList<SurveyData>,serachWord: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        init{
            FirebaseDatabase.getInstance().getReference("/Surveys")
                .orderByChild("title").addValueEventListener(object :ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(snapshot in snapshot!!.children){
                            Log.d("MP","snapshot: "+snapshot.value)
                            val survey : SurveyData? = snapshot.getValue(SurveyData::class.java)
                            Log.d("MP","snapshot.title: "+survey?.title)
                            if((survey!!.title).contains(serachWord)){
                                surveys.add(survey)
                                Log.d("MP","surveys: "+surveys)
                            }
                        }
                        notifyDataSetChanged()
                    }
                })

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.SearchViewHolder {
            return SearchViewHolder(ItemMainBinding.inflate(LayoutInflater.from(context),parent, false))
        }
        override fun getItemCount(): Int {
            return surveys.size
        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val binding = (holder as SearchAdapter.SearchViewHolder).binding
            val survey = surveys[position]

            binding.titleText.text = survey.title
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
        inner class SearchViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root){
            //설문지 제목 텍스트 뷰
            val titleText : TextView = binding.titleText
            //설문지 기간 텍스트 뷰
            val dateText : TextView = binding.dateText
            // 북마크 이미지뷰
            //val bookmarkView : ImageView = itemView.bookmarkView
        }
    }
    // Basic Adapter class
    inner class MyAdapter(val surveys: MutableList<SurveyData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        init{
            //Get survey data from firebase
            FirebaseDatabase.getInstance().getReference("/Surveys")
                .orderByChild("startDate").addChildEventListener(object : ChildEventListener {
                    //When survey is added
                    override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                        snapshot?.let{snapshot->
                            //Get snapshot's data to 'survey' instance
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let{
                                //When a new survey is added at the end of recyclerview
                                if (prevChildKey == null){
                                    //survey is added to surveys
                                    surveys.add(it)
                                    // Notify that a survey is added to adapter of recyclerview
                                    notifyItemInserted(surveys.size -1)
                                }else{
                                    // When the post is added in the middle, we find the location of data ahead of this using prevChildKey and add it
                                    val prevIndex = surveys.map {it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex+1,survey)
                                    //Notify that a survey is added to adapter of recyclerview
                                    notifyItemInserted(prevIndex+1)
                                }
                            }
                        }
                    }
                    // When survey is changed
                    override fun onChildChanged(snapshot: DataSnapshot, prevChildKey: String?) {
                        snapshot?.let { snapshot ->
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let { survey ->
                                // When the survey is changed, change data to index of the data ahead of the post
                                val prevIndex = surveys.map { it.surveyId }.indexOf(prevChildKey)
                                surveys[prevIndex + 1] = survey
                                notifyItemChanged(prevIndex + 1)
                            }
                        }
                    }
                    //When survey is removed
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        snapshot?.let {
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let { survey ->
                                // Find existing stored index and Delete the data correspond to the index
                                val existIndex = surveys.map { it.surveyId }.indexOf(survey.surveyId)
                                surveys.removeAt(existIndex)
                                notifyItemRemoved(existIndex)
                            }
                        }
                    }
                    // When order of survey is changed(moved)
                    override fun onChildMoved(snapshot: DataSnapshot, prevChildKey: String?) {
                        snapshot?.let{
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let{survey->
                                //Find existing index
                                val existIndex = surveys.map{it.surveyId}.indexOf(survey.surveyId)
                                //Delete existing index
                                surveys.removeAt(existIndex)
                                notifyItemRemoved(existIndex)
                                //When prevChildKey is null, deal with the survey that was moved to last
                                if (prevChildKey == null){
                                    surveys.add(survey)
                                    notifyItemChanged(surveys.size-1)
                                }else{
                                    //add survey next to prevChildKey
                                    val prevIndex = surveys.map{it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex + 1, survey)
                                    notifyItemChanged(prevIndex+1)
                                }
                            }
                        }
                    }
                    // When change is cancelled
                    override fun onCancelled(error: DatabaseError) {
                        //error?.toException()?.printStackTrace()
                    }
                })

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(context),parent, false))
        }

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

        // end, on survey


    }
    @RequiresApi(Build.VERSION_CODES.O)
    // When stored value of endDate >= currentDate
    inner class onAdapter(val surveys: MutableList<SurveyData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        init{
            //firebase에서 survey 데이터를 가져온 후 surveys 변수에 저장
            FirebaseDatabase.getInstance().getReference("/Surveys")
                .orderByChild("endDate").startAt(today).addChildEventListener(object : ChildEventListener {
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
        fun search(serachWord : String) {
            FirebaseDatabase.getInstance().reference.child("/Surveys").orderByChild("title")
                //.startAt(serachWord).endAt(serachWord+"\uf8ff")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //val value = snapshot.getValue<SurveyData>()
                        Log.d("MP","surveys 초기화")
                        surveys.clear()
                        for(snapshot in snapshot!!.children){
                            Log.d("MP","snapshot: "+snapshot.value)
                            val survey : SurveyData? = snapshot.getValue(SurveyData::class.java)
                            Log.d("MP","snapshot.title: "+survey?.title)
                            if((survey!!.title).contains(serachWord)){
                                surveys.add(survey)
                                Log.d("MP","surveys: "+surveys)
                            }
                        }
                        notifyDataSetChanged()
                        //notifyDataSetChanged()를 선언함으로써 변경사항이 저장된다..ㅠㅠㅠ
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })

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

        // end, on survey


    }
    // When stored value of endDate < currentDate 현재날짜가 클 때
    @RequiresApi(Build.VERSION_CODES.O)
    inner class finAdapter(val surveys: MutableList<SurveyData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        init{
            //firebase에서 survey 데이터를 가져온 후 surveys 변수에 저장
            FirebaseDatabase.getInstance().getReference("/Surveys")
                .orderByChild("endDate").endBefore(today).addChildEventListener(object : ChildEventListener {
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

        // end, on survey


    }


}