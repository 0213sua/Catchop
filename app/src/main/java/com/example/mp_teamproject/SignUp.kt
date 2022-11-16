package com.example.mp_teamproject

import android.R
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mp_teamproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUp : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.SUSignupBtn.setOnClickListener {
            createAccount(
                binding.SignupID.text.toString().trim(),
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
                    Toast.makeText(this,"회원가입 성공", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser

                } else {
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"회원가입 실패", Toast.LENGTH_SHORT).show()
            }
    }


}