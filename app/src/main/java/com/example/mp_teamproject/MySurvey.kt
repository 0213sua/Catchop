package com.example.mp_teamproject

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mp_teamproject.databinding.ActivityMySurveyBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.item_main.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MySurvey : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    val binding by lazy { ActivityMySurveyBinding.inflate(layoutInflater)}


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

        val layoutManager = LinearLayoutManager(this@MySurvey)

        layoutManager.reverseLayout = true

        layoutManager.stackFromEnd = true

        binding.MrecyclerView.layoutManager = layoutManager
        binding.MrecyclerView.adapter = MyAdapter()

        binding.arrow.setOnClickListener {
            finish()
        }
        binding.home.setOnClickListener {
            val intent = Intent(this@MySurvey,HomeFragment::class.java)
            startActivity(intent)
        }
        FirebaseDatabase.getInstance().getReference("/Surveys")
            .orderByChild("writerId").equalTo(userid).addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot.let { snapshot ->
                        val survey = snapshot.getValue(SurveyData::class.java)
                        survey?.let{

                            if (prevChildKey == null){

                                surveys.add(it)

                                binding.MrecyclerView.adapter?.notifyItemInserted(surveys.size -1)
                            }else{

                                val prevIndex = surveys.map {it.surveyId}.indexOf(prevChildKey)
                                surveys.add(prevIndex+1,survey)

                                binding.MrecyclerView.adapter?.notifyItemInserted(prevIndex+1)
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
                            binding.MrecyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                        }
                    }
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot?.let {

                        val survey = snapshot.getValue(SurveyData::class.java)

                        survey?.let { survey ->

                            val existIndex = surveys.map { it.surveyId }.indexOf(survey.surveyId)
                            surveys.removeAt(existIndex)
                            binding.MrecyclerView.adapter?.notifyItemRemoved(existIndex)
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
                            binding.MrecyclerView.adapter?.notifyItemRemoved(existIndex)

                            if (prevChildKey == null){
                                surveys.add(survey)
                                binding.MrecyclerView.adapter?.notifyItemChanged(surveys.size-1)
                            }else{

                                val prevIndex = surveys.map{it.surveyId}.indexOf(prevChildKey)
                                surveys.add(prevIndex + 1, survey)
                                binding.MrecyclerView.adapter?.notifyItemChanged(prevIndex+1)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    error?.toException()?.printStackTrace()
                }
            })
    }
    inner class MyViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleText : TextView = itemView.titleText
        val dateText : TextView = itemView.dateText

    }

// RecyclerView 의 어댑터 클래스
    inner class MyAdapter : RecyclerView.Adapter<MyViewHodler>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHodler {
            return MyViewHodler(
                LayoutInflater.from(this@MySurvey).inflate(R.layout.item_main,
                parent, false))
        }

        override fun getItemCount(): Int {
            return surveys.size
        }


        override fun onBindViewHolder(holder: MyViewHodler, position: Int) {
            val survey = surveys[position]

            holder.titleText.text = survey.title

            holder.dateText.text = survey.startDate+" ~ "+survey.endDate


            holder.itemView.setOnClickListener {

                val intent = Intent(this@MySurvey, SurveyInfo::class.java)

                intent.putExtra("surveyId", survey.surveyId)

                startActivity(intent)
            }
        }
    }


}


