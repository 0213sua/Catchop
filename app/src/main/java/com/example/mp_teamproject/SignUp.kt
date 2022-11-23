package com.example.mp_teamproject

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.mp_teamproject.databinding.ActivitySignUpBinding
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val actionBar: ActionBar? = supportActionBar

        actionBar?.setTitle("Create Account")


        actionBar?.setDisplayHomeAsUpEnabled(true) //뒤로가기버튼

        actionBar?.setDisplayShowHomeEnabled(true)



        val name = binding.SUNameEditText.text.toString()
        val email = binding.SUEmailEditText.text.toString()
        val pw = binding.SUPwEditText.text.toString()
        val phone = binding.SUPhoneEditText.text.toString()

        val auth = FirebaseAuth.getInstance()



        binding.SUSignupBtn.setOnClickListener {
//            if (TextUtils.isEmpty(binding.SUNameEditText.getText())) {
//                Toast.makeText(this,"Plz put your name", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener }
//            if (TextUtils.isEmpty(binding.SUEmailEditText.getText())) {
//                Toast.makeText(this,"Plz put your email", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener }
//            if (TextUtils.isEmpty(binding.SUPhoneEditText.getText())) {
//                Toast.makeText(this,"Plz put your phone number", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener }

//            auth.createUserWithEmailAndPassword(email,pw)
//                .addOnCompleteListener(this, object: OnCompleteListener<AuthResult> {
//                    override fun onComplete(p0: Task<AuthResult>) {
//                        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(콜) || TextUtils.isEmpty(pw)) {
//                            Toast.makeText(this@SignUp, "비어있습니다", Toast.LENGTH_SHORT).show()
//                        }
//
//                        val firebaseUser: FirebaseUser? = auth.currentUser
//                        val userid = firebaseUser?.uid
//                        val reference = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!)
//                        val hashMap: HashMap<String, Any> = HashMap()
//
//                        hashMap["id"] = userid
//                        hashMap["username"] = name
//                        hashMap["email"] = email
//                        hashMap["pw"] = pw
//                        hashMap["phone"] = phone
//
//
//                    }
//                })


            createAccount(
                binding.SUEmailEditText.text.toString().trim(),
                binding.SUPwEditText.text.toString().trim()
            )
        }

        binding.SUPhoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

//        binding.SUPwcheckEditText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//                if (binding.SUPwEditText.getText().toString().trim()
//                        .equals(binding.SUPwcheckEditText.getText().toString().trim())
//                ) {
//                    binding.SUPwchecktextText.setText("Password matches ! :)")
//                    // 가입하기 버튼 활성화
//                    binding.SUSignupBtn.isEnabled = true
//                } else {
//                    binding.SUPwchecktextText.setText("Password doesn't match .. :( ")
//                    // 가입하기 버튼 비활성화
//                    binding.SUSignupBtn.isEnabled = false
//                }
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
//        })


    }


    private fun createAccount(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
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