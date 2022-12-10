package com.example.mp_teamproject

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.mp_teamproject.databinding.ActivitySignUpBinding
import com.example.mp_teamproject.databinding.FragmentHomeBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_profile.*


class SignUp : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    private val binding by lazy{ ActivitySignUpBinding.inflate(layoutInflater)}
    //var hashMap = HashMap<String,Any> ()

    var sex : String = ""
    var job : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle("Create Account")
        actionBar?.setDisplayHomeAsUpEnabled(true) //뒤로가기버튼
        actionBar?.setDisplayShowHomeEnabled(true)


        binding.SUJobSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2==0){
                    job = ""
                }
                if(p2==1){
                    job= "학생"
                }
                if(p2==2){
                    job= "대학생"
                }
                if(p2==3){
                    job= "대학원생"
                }
                if(p2==4){
                    job= "교수"
                }
                if(p2==5){
                    job= "인턴"
                }
                if(p2==6){
                    job= "직장인"
                }
                if(p2==7){
                    job= "프리랜서"
                }
                if(p2==8){
                    job= "기타"
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }



        binding.SUSignupBtn.setOnClickListener {

            when(binding.SUSexRadio.checkedRadioButtonId){
                binding.SexMale.id -> sex = "male"
                else -> sex = "female"
            }

            val name = binding.SUNameEditText.text.toString().trim()
            val birth = binding.SUBirthdateEditText.text.toString().trim()
            val email = binding.SUEmailEditText.text.toString().trim()
            val password = binding.SUPwEditText.text.toString().trim()
            val phone = binding.SUPhoneEditText.text.toString().trim()
            createUser(name,sex,birth,email,password,phone,job)



        }

        binding.SUPhoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.SUPwcheckEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (binding.SUPwEditText.getText().toString().trim()
                        .equals(binding.SUPwcheckEditText.getText().toString().trim())
                ) {
                    binding.SUPwchecktextText.setText("Password matches ! :)")
                    // 가입하기 버튼 활성화
                    binding.SUSignupBtn.isEnabled = true
                } else {
                    binding.SUPwchecktextText.setText("Password doesn't match .. :( ")
                    // 가입하기 버튼 비활성화
                    binding.SUSignupBtn.isEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })


    }

    fun createUser(name : String,sex : String, birth:String, email : String, password : String, phone : String, job : String) {
        auth = FirebaseAuth.getInstance()




        Log.d("ITM"," $name, $email, $password, $phone")

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val userid = auth!!.currentUser?.uid
                        val reference = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!)
                        val hashMap: HashMap<String, Any> = HashMap()


                        hashMap["id"] = userid
                        hashMap["username"] = name
                        hashMap["sex"] = sex
                        hashMap["birth"] = birth
                        hashMap["email"] = email
                        hashMap["pw"] = password
                        hashMap["phone"] = phone
                        hashMap["job"] = job
                        reference.setValue(hashMap)

                        Toast.makeText(this, "Registration Success :)",Toast.LENGTH_SHORT).show()
                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "Registration Failed :(",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(this, "Registration Failed :(", Toast.LENGTH_SHORT).show() }
    }

}
