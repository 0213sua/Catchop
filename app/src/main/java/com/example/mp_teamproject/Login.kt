package com.example.mp_teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mp_teamproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class Login : AppCompatActivity() {
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

//        binding.LoginBtn.setOnClickListener{
//            val intent = Intent(this,Main::class.java)
//            startActivity(intent)
//        }
        binding.LoginBtn.setOnClickListener{
            logIn(binding.IDEditText.text.toString().trim(),binding.PWEditText.text.toString().trim())
        }

        binding.LISignUpBtn.setOnClickListener{
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }
    }
    private fun logIn(email : String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext, "로그인에 성공 하였습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        moveMainPage(auth?.currentUser)
                    } else {
                        Toast.makeText(
                            baseContext, "로그인에 실패 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    fun moveMainPage(user: FirebaseUser?) {
        if (user!= null) {
            val intent = Intent(this,Main::class.java)
//            val intent = Intent(this,SurveyListTest::class.java)
            startActivity(intent)
            finish()
        }
    }
}