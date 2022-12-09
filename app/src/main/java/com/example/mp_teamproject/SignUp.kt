package com.example.mp_teamproject

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
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


class SignUp : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    private val binding by lazy{ ActivitySignUpBinding.inflate(layoutInflater)}
    //var hashMap = HashMap<String,Any> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle("Create Account")
        actionBar?.setDisplayHomeAsUpEnabled(true) //뒤로가기버튼
        actionBar?.setDisplayShowHomeEnabled(true)




        binding.SUSignupBtn.setOnClickListener {

            val name = binding.SUNameEditText.text.toString().trim()
            val email = binding.SUEmailEditText.text.toString().trim()
            val password = binding.SUPwEditText.text.toString().trim()
            val phone = binding.SUPhoneEditText.text.toString().trim()
            createUser(name,email,password,phone)



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

    fun createUser(name : String, email : String, password : String, phone : String) {
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
                        hashMap["email"] = email
                        hashMap["pw"] = password
                        hashMap["phone"] = phone
                        reference.setValue(hashMap)

                        Toast.makeText(this, "계정 생성 완료.",Toast.LENGTH_SHORT).show()
                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(this, "Member registration Failed !", Toast.LENGTH_SHORT).show() }
    }




}

