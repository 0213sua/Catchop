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

            // if click card, call DetailActivity
            binding.itemRoot.setOnClickListener {
                // intent to call survey information page
                val intent = Intent(context, SurveyInfo::class.java)
                intent.putExtra("surveyId", survey.surveyId)
                startActivity(intent)
            }
        }
        inner class SearchViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root){
            val titleText : TextView = binding.titleText
            val dateText : TextView = binding.dateText
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

            binding.titleText.text = survey.title

            binding.dateText.text = survey.startDate+" ~ "+survey.endDate

            binding.itemRoot.setOnClickListener {

                val intent = Intent(context, SurveyInfo::class.java)
                intent.putExtra("surveyId", survey.surveyId)
                startActivity(intent)
            }
        }
        inner class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root){

            val titleText : TextView = binding.titleText
            val dateText : TextView = binding.dateText
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    // When stored value of endDate >= currentDate
    inner class onAdapter(val surveys: MutableList<SurveyData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        init{
            // store survey data from firebase to surveys value
            FirebaseDatabase.getInstance().getReference("/Surveys")
                .orderByChild("endDate").startAt(today).addChildEventListener(object : ChildEventListener {
                    //when add survey
                    override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                        snapshot?.let{snapshot->
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let{
                                if (prevChildKey == null){
                                    surveys.add(it)
                                    notifyItemInserted(surveys.size -1)
                                }else{
                                    val prevIndex = surveys.map {it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex+1,survey)
                                    notifyItemInserted(prevIndex+1)
                                }
                            }
                        }
                    }
                    // when surveys changed
                    override fun onChildChanged(snapshot: DataSnapshot, prevChildKey: String?) {
                        snapshot?.let { snapshot ->

                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let { survey ->
                                val prevIndex = surveys.map { it.surveyId }.indexOf(prevChildKey)
                                surveys[prevIndex + 1] = survey
                                notifyItemChanged(prevIndex + 1)
                            }
                        }
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        snapshot?.let {

                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let { survey ->
                                val existIndex = surveys.map { it.surveyId }.indexOf(survey.surveyId)
                                surveys.removeAt(existIndex)
                                notifyItemRemoved(existIndex)
                            }
                        }
                    }
                    // when survey moved
                    override fun onChildMoved(snapshot: DataSnapshot, prevChildKey: String?) {
                        // snapshot
                        snapshot?.let{

                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let{survey->
                                val existIndex = surveys.map{it.surveyId}.indexOf(survey.surveyId)
                                surveys.removeAt(existIndex)
                                notifyItemRemoved(existIndex)

                                if (prevChildKey == null){
                                    surveys.add(survey)
                                    notifyItemChanged(surveys.size-1)
                                }else{
                                    val prevIndex = surveys.map{it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex + 1, survey)
                                    notifyItemChanged(prevIndex+1)
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }


                })

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(context),parent, false))
        }

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
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val binding = (holder as MyViewHolder).binding
            val survey = surveys[position]

            binding.titleText.text = survey.title

            binding.dateText.text = survey.startDate+" ~ "+survey.endDate

            binding.itemRoot.setOnClickListener {

                val intent = Intent(context, SurveyInfo::class.java)

                intent.putExtra("surveyId", survey.surveyId)

                startActivity(intent)
            }
        }
        inner class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root){

            val titleText : TextView = binding.titleText

            val dateText : TextView = binding.dateText
        }


    }
    // When stored value of endDate < currentDate
    @RequiresApi(Build.VERSION_CODES.O)
    inner class finAdapter(val surveys: MutableList<SurveyData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        init{
            FirebaseDatabase.getInstance().getReference("/Surveys")
                .orderByChild("endDate").endBefore(today).addChildEventListener(object : ChildEventListener {

                    override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                        snapshot?.let{snapshot->

                            val survey = snapshot.getValue(SurveyData::class.java)

                            survey?.let{

                                if (prevChildKey == null){

                                    surveys.add(it)
                                    notifyItemInserted(surveys.size -1)
                                }else{

                                    val prevIndex = surveys.map {it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex+1,survey)
                                    notifyItemInserted(prevIndex+1)
                                }
                            }
                        }
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, prevChildKey: String?) {
                        snapshot?.let { snapshot ->
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let { survey ->

                                val prevIndex = surveys.map { it.surveyId }.indexOf(prevChildKey)
                                surveys[prevIndex + 1] = survey
                                notifyItemChanged(prevIndex + 1)
                            }
                        }
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        snapshot?.let {
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let { survey ->
                                val existIndex = surveys.map { it.surveyId }.indexOf(survey.surveyId)
                                surveys.removeAt(existIndex)
                                notifyItemRemoved(existIndex)
                            }
                        }
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, prevChildKey: String?) {
                        // snapshot
                        snapshot?.let{
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let{survey->

                                val existIndex = surveys.map{it.surveyId}.indexOf(survey.surveyId)

                                surveys.removeAt(existIndex)
                                notifyItemRemoved(existIndex)

                                if (prevChildKey == null){
                                    surveys.add(survey)
                                    notifyItemChanged(surveys.size-1)
                                }else{
                                    val prevIndex = surveys.map{it.surveyId}.indexOf(prevChildKey)
                                    surveys.add(prevIndex + 1, survey)
                                    notifyItemChanged(prevIndex+1)
                                }
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
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

            binding.titleText.text = survey.title

            binding.dateText.text = survey.startDate+" ~ "+survey.endDate


            binding.itemRoot.setOnClickListener {

                val intent = Intent(context, SurveyInfo::class.java)

                intent.putExtra("surveyId", survey.surveyId)
                startActivity(intent)
            }
        }
        inner class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root){
            val titleText : TextView = binding.titleText
            val dateText : TextView = binding.dateText
        }
    }


}