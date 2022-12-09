package com.example.mp_teamproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mp_teamproject.databinding.FragmentHomeBinding
import com.example.mp_teamproject.databinding.FragmentMytreeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MytreeFragment : Fragment() {
    private var auth : FirebaseAuth? = null
    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        num = 0
        val binding = FragmentMytreeBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        val userid = auth!!.currentUser?.uid

        //var num_of_survey:Int = 1

        FirebaseDatabase.getInstance().getReference("/Surveys")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot!!.children) {
                        snapshot?.let { snapshot ->
                            val survey = snapshot.getValue(SurveyData::class.java)
                            survey?.let {
                                Log.d("ITM", "snapshot's id" + it.surveyId)
                                if (it.surveyorInfo.contains(userid.toString())) {
                                    num = num + 1
                                }
                            }

                        }
                        Log.d("ITM", "참여설문 개수" + num)

                    }
                    Log.d("ITM", "반복문 후 최종 참여 설문 개수" + num)
                    binding.numSurvey.text = num.toString()
                    binding.lnum.text = (10-num).toString()

                }
            })

        Log.d("ITM", "최종 num 확인" + num)

//
//        Log.d("ITM", "바뀌기 전 참여개수 확인" + binding.numSurvey.text)
//        binding.numSurvey.text = num.toString()
//        Log.d("ITM", "바뀐 참여개수 확인" + binding.numSurvey.text)
//        binding.lnum.text = (10-num).toString()

        if(num < 10){
            binding.imageView.setImageResource(R.drawable.mini_tree)
        }else{
            binding.imageView.setImageResource(R.drawable.tree)
        }

        return binding.root
    }

    companion object {
        fun newInstance():MytreeFragment{
            return MytreeFragment()
        }
    }
}