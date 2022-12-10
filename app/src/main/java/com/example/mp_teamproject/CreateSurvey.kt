package com.example.mp_teamproject

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mp_teamproject.databinding.ActivityCreateSurveyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*



class CreateSurvey : AppCompatActivity() {
    val binding by lazy{ ActivityCreateSurveyBinding.inflate(layoutInflater)}
    private var auth : FirebaseAuth? = null

    // calendar
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        val userid = auth!!.currentUser?.uid

        // surveyData obejct
        val survey = SurveyData()
        // create new key to store object in 'Surveys' in firebase
        val newRef = FirebaseDatabase.getInstance().getReference("Surveys").push()

        // spinner
        val myList = listOf("","Administration","Technology","Education","Logistics","Land managing","Agri-Animal-Fish","Culture & Tourism","Health","Food")
        //provide views for an AdapterView
        val myAdapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, myList)

        // connect to google form page
        binding.csAddBtn.setOnClickListener {
            // save implict intent(ACTION_VIEW) & pass uri string(github address)
            val add = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/u/0/?tgif=d"))
            startActivity(add)
        }

        // connect myAdapter to the Spinner
        binding.categorySpinner.adapter = myAdapter
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2==0){
                    survey.category = ""
                }
                if(p2==1){
                    survey.category = "Administration"
                }
                if(p2==2){
                    survey.category = "Technology"
                }
                if(p2==3){
                    survey.category = "Education"
                }
                if(p2==4){
                    survey.category = "Logistics"
                }
                if(p2==5){
                    survey.category = "Land managing"
                }
                if(p2==6){
                    survey.category = "Agri-Animal-Fish"

                }
                if(p2==7){
                    survey.category = "Culture & Tourism"

                }
                if(p2==8){
                    survey.category = "Health"

                }
                if(p2==9){
                    survey.category = "Food"
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        //original value
        binding.csRg.check(binding.CSYesRadio.id)

        binding.registerBtn.setOnClickListener {

            if(TextUtils.isEmpty(binding.titleText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(binding.writerText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(binding.InstitutionText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(binding.purposeText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(binding.startDate.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(binding.endDate.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(binding.contentText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(binding.uriText.text.toString())){
                Toast.makeText(applicationContext, "Plz fill all blank.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Assign the id of the questionnaire as the key for the newly created Firebase reference
            survey.surveyId = newRef.key.toString()

            // Assign the email using login information
            var auth : FirebaseAuth? = null
            auth = Firebase.auth
            survey.writerId = auth?.currentUser?.uid.toString()

            // allocate input edit text to survey object
            survey.title = binding.titleText.text.toString()
            survey.writer = binding.writerText.text.toString()
            survey.institution = binding.InstitutionText.text.toString()
            survey.purpose = binding.purposeText.text.toString()

            survey.startDate = binding.startDate.text.toString()
            survey.endDate = binding.endDate.text.toString()

            //radio btn
            when(binding.csRg.checkedRadioButtonId){
                binding.CSNoRadio.id -> survey.resultOpen = "NO"
                else -> survey.resultOpen = "YES"
            }

            survey.surveyorInfo  = userid.toString()
            Log.d("ITM","SUA, PLEASE STORE,, surveyorInfo $userid")
            survey.surveyContent = binding.contentText.text.toString()
            survey.uri = binding.uriText.text.toString()
            // store surveyData object with new reference
            newRef.setValue(survey)

            Toast.makeText(applicationContext,"Created your survey", Toast.LENGTH_SHORT).show()
            finish()
        }

        // calendar
        binding.startDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, {_,year,month, day -> binding.startDate.text = year.toString()+"."+(month+1).toString()+"."+day.toString()}, year, month, day)
            datePickerDialog.show()
        }
        binding.endDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, {_,year,month, day -> binding.endDate.text = year.toString()+"."+(month+1).toString()+"."+day.toString()}, year, month, day)
            datePickerDialog.show()
        }

    }

}