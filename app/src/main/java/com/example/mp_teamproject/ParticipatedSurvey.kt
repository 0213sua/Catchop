package com.example.mp_teamproject

import android.content.Context
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
import com.example.mp_teamproject.databinding.ActivityParticipatedSurveyBinding
import com.example.mp_teamproject.databinding.FragmentHomeBinding
import com.example.mp_teamproject.databinding.ItemMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.item_main.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ParticipatedSurvey : AppCompatActivity() {
    val binding by lazy { ActivityParticipatedSurveyBinding.inflate(layoutInflater) }
    private var auth: FirebaseAuth? = null

    private lateinit var databaseReference: DatabaseReference
    val surveys: MutableList<SurveyData> = mutableListOf()
    var userid = ""
    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val today = current.format(formatter)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        userid = auth!!.currentUser?.uid.toString()
        Log.d("MP","userid : $userid")
        val layoutManager = LinearLayoutManager(this@ParticipatedSurvey)
        // arrange recyclerview's item in revise order
        layoutManager.reverseLayout = true

        layoutManager.stackFromEnd = true

        binding.PrecyclerView.layoutManager = layoutManager

        surveys.clear()

        binding.arrow.setOnClickListener {
//            val intent = Intent(this@ParticipatedSurvey,MypageFragment::class.java)
//            startActivity(intent)
            finish()
        }

        binding.PrecyclerView.adapter = MyAdapter()

        FirebaseDatabase.getInstance().getReference("/Surveys")
            .orderByChild("startDate").addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot?.let {snapshot->

                        val survey = snapshot.getValue(SurveyData::class.java)
                        survey?.let{

                            if (prevChildKey == null){

                                surveys.add(it)

                                binding.PrecyclerView.adapter?.notifyItemInserted(surveys.size -1)
                            }else{

                                val prevIndex = surveys.map {it.surveyId}.indexOf(prevChildKey)
                                surveys.add(prevIndex+1,survey)
                                binding.PrecyclerView.adapter?.notifyItemInserted(prevIndex+1)
                            }
                        }

                    }
                }
                // ???????????? ????????? ??????
                override fun onChildChanged(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot?.let { snapshot ->

                        val survey = snapshot.getValue(SurveyData::class.java)
                        survey?.let { survey ->
                            val prevIndex = surveys.map { it.surveyId }.indexOf(prevChildKey)
                            surveys[prevIndex + 1] = survey
                            binding.PrecyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot?.let {

                        val survey = snapshot.getValue(SurveyData::class.java)
                        survey?.let { survey ->
                            val existIndex = surveys.map { it.surveyId }.indexOf(survey.surveyId)
                            surveys.removeAt(existIndex)
                            binding.PrecyclerView.adapter?.notifyItemRemoved(existIndex)
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
                            binding.PrecyclerView.adapter?.notifyItemRemoved(existIndex)
                            if (prevChildKey == null){
                                surveys.add(survey)
                                binding.PrecyclerView.adapter?.notifyItemChanged(surveys.size-1)
                            }else{

                                val prevIndex = surveys.map{it.surveyId}.indexOf(prevChildKey)
                                surveys.add(prevIndex + 1, survey)
                                binding.PrecyclerView.adapter?.notifyItemChanged(prevIndex+1)
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    error?.toException()?.printStackTrace()
                }
            })

        (binding.PrecyclerView.adapter as ParticipatedSurvey.MyAdapter).search(userid)
    }

    inner class MyViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleText : TextView = itemView.titleText
        val dateText : TextView = itemView.dateText

    }


    inner class MyAdapter : RecyclerView.Adapter<MyViewHodler>() {

        // RecyclerView ?????? ??? Row(???)?????? ?????? ViewHolder ??? ???????????? ????????? ?????????
        fun search(serachWord : String) {
            FirebaseDatabase.getInstance().getReference("/Surveys").orderByChild("surveyorInfo")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        surveys.clear()
                        for(snapshot in snapshot!!.children){
                            Log.d("MP","snapshot: "+snapshot.value)
                            val survey : SurveyData? = snapshot.getValue(SurveyData::class.java)
                            Log.d("MP","snapshot.surveyorInfo: "+survey?.surveyorInfo)
                            if((survey!!.surveyorInfo).contains(serachWord)){
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHodler {
            return MyViewHodler(
                LayoutInflater.from(this@ParticipatedSurvey).inflate(R.layout.item_main,
                    parent, false))
        }

        // RecyclerView ?????? ????????? ?????? ????????? ????????? ?????? ?????????
        override fun getItemCount(): Int {
            return surveys.size
        }

        // ??? ?????? ??????????????? ???????????? ViewHolder UI??? ???????????? ???????????? ?????????
        override fun onBindViewHolder(holder: MyViewHodler, position: Int) {
            val survey = surveys[position]
            // ????????? ?????? ??????
            holder.titleText.text = survey.title
            // ?????? ????????? ??????
            holder.dateText.text = survey.startDate+" ~ "+survey.endDate

            // ????????? ???????????? ?????? DetailActivity ??? ????????????.
            holder.itemView.setOnClickListener {
                // ??????????????? ????????? Intent ??? ????????????.
                val intent = Intent(this@ParticipatedSurvey, SurveyInfo::class.java)
                // ????????? ????????? ID ????????? intent ??? ????????????.
                intent.putExtra("surveyId", survey.surveyId)
                // intent ??? ??????????????? ????????????.
                startActivity(intent)
            }
        }
    }
}

