package com.example.mp_teamproject

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.mp_teamproject.databinding.ActivityCreateSurveyBinding
//import com.google.firebase.ktx.Firebase


class CreateSurvey : AppCompatActivity() {
    val binding by lazy{ ActivityCreateSurveyBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myList = listOf("Administration","Technology","Education","Traffic","Land managing","Agriculture","Culture","Health","Food")

        //provide views for an AdapterView
        val myAdapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, myList)

        // connect myAdapter to the Spinner
        binding.categorySpinner.adapter = myAdapter
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                // if user choose ISLAB, then islab intent will be start to move to the IslabActivity
//                if(p2==1){
//                    startActivity(islab)
//                }
//                // if user choose Design thinking, then design intent will be start to move to the DesignActivity
//                if(p2==2){
//                    startActivity(design)
//                }
//                // if user choose Interest, then interest intent will be start to move to the InterestActivity
//                if(p2==3){
//                    startActivity(interest)
//                }
            }


            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
//        val database = Firebase.database
//        val myRef = database.getReference("message")
//
//        myRef.setValue("Hello, World!")
//
//        // Read from the database
//        myRef.addValueEventListener(object: ValueEventListener() {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                val value = snapshot.getValue<String>()
//                Log.d(TAG, "Value is: " + value)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w(TAG, "Failed to read value.", error.toException())
//            }
//
//        })
    }
}