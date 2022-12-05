package com.example.mp_teamproject

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.mp_teamproject.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


class MypageFragment : Fragment() {
    private var auth : FirebaseAuth? = null
    lateinit var binding: FragmentMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


        val userid = auth!!.currentUser?.uid
        //val reference = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!)
        Log.d("ITM","THIS ! $userid ")
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

        // profile image update
        Log.d("ee","This is mypage fragment!")

        // bundle로 전달받은 값이 있을 때만
        if(arguments?.getString("passUri") != null){
            //val getImg = Uri.parse(arguments?.getString("imgUri"))
            val getImg = arguments?.getString("passUri") // getImg를 리스트로 만들어서 제일 마지막꺼를 가져다가 imageView에 binding해야겠다! 들어갈때마다 reset돼 (string으로 받아도 상관없으면 string list로)
            Log.d("ee","get Uri : $getImg")
            val changeImg = Uri.parse(getImg)
            binding.mProfileImg.setImageURI(changeImg)
        }


        binding.MPSIPBtn.setOnClickListener {
            //내가 만든 설문지 화면으로 넘어감

            val intent = Intent(this@MypageFragment.requireContext(),MySurvey::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }
        binding.MPSIABtn.setOnClickListener {
            //내가 참여한 설문지 화면
            val intent = Intent(this@MypageFragment.requireContext(),ParticipatedSurvey::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        binding.MPEditprofileBtn.setOnClickListener {
            val intent = Intent(this@MypageFragment.requireContext(),EditProfile::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }

        binding.MPLogoutBtn.setOnClickListener {

            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("YES",
                    DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this@MypageFragment.requireContext(),Login::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        auth?.signOut()
                        Toast.makeText( this.context, "Logout Success !", Toast.LENGTH_SHORT
                        ).show()
                    })
                .setNegativeButton("NO",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            // show dialog
            builder.show()

        }

        binding.MPDeleteAbtn.setOnClickListener {
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("계정 삭제")
                .setMessage("정말로 케첩의 계정을 삭제하시겠습니까?")
                .setPositiveButton("YES",
                    DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this@MypageFragment.requireContext(),Login::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        auth?.currentUser?.delete()
                        Toast.makeText( this.context, "Withdraw Success !", Toast.LENGTH_SHORT
                        ).show()
                    })
                .setNegativeButton("NO",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            // show dialog
            builder.show()

        }

        return binding.root
    }



}