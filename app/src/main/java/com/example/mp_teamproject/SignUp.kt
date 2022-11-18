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
import com.google.firebase.auth.FirebaseAuth


class SignUp : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.setTitle("Create Account")


        actionBar?.setDisplayHomeAsUpEnabled(true) //뒤로가기버튼

        actionBar?.setDisplayShowHomeEnabled(true)

        auth = FirebaseAuth.getInstance()


        binding.SUSignupBtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.SUNameEditText.getText())) {
                Toast.makeText(this,"Plz put your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }
            if (TextUtils.isEmpty(binding.SUEmailEditText.getText())) {
                Toast.makeText(this,"Plz put your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }
            if (TextUtils.isEmpty(binding.SUPhoneEditText.getText())) {
                Toast.makeText(this,"Plz put your phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }


            createAccount(
                binding.SUEmailEditText.text.toString().trim(),
                binding.SUPwcheckEditText.text.toString().trim()
            )
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


    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Member registration Successful !", Toast.LENGTH_SHORT).show()
                    //val firebaseUser: FirebaseUser? = auth.currentUser

//                    val user = auth.currentUser
//                    val hashMap: HashMap<String, Any> = HashMap()
//                    hashMap["email"] = email
//                    hashMap["pw"] = password
//                    hashMap["name"] = name


                } else {
                    Toast.makeText(this, "Member registration Failed !", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Member registration Failed !", Toast.LENGTH_SHORT).show()
            }
    }


}