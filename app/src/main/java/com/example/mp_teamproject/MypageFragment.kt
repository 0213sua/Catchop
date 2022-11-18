package com.example.mp_teamproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mp_teamproject.databinding.ActivityLoginBinding
import com.example.mp_teamproject.databinding.FragmentCategoryBinding
import com.example.mp_teamproject.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MypageFragment : Fragment() {
    private var auth : FirebaseAuth? = null
    lateinit var binding: FragmentMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(layoutInflater)

        binding.MPLogoutBtn.setOnClickListener {
            val intent = Intent(this@MypageFragment.requireContext(),Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            auth?.signOut()
            Toast.makeText( this.context, "로그아웃되었습니다.", Toast.LENGTH_SHORT
            ).show()
        }

        binding.MPDeleteABtn.setOnClickListener {
            val intent = Intent(this@MypageFragment.requireContext(),Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            auth?.currentUser?.delete()
            Toast.makeText( this.context, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }


}