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
import com.example.mp_teamproject.databinding.ActivitySignUpBinding
import com.example.mp_teamproject.databinding.FragmentMypageBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


class MypageFragment : Fragment() {
    private var auth : FirebaseAuth? = null
    private val binding by lazy{ FragmentMypageBinding.inflate(layoutInflater)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


        val userid = auth!!.currentUser?.uid
        val reference = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("username")
        Log.d("ITM","HIHIHI reference !! $reference")



        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue()
                binding.MPNickNameTxt.text = value.toString()
                Log.d("ITM", "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ITM", "Failed to read value.", error.toException())
            }
        })



        Log.d("ITM","아임 수아수 $reference")


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

        //save preference with getted intent
        val getImg = arguments?.getString("passUri")
        if (getImg !=null){
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            if (sharedPref != null) {
                with (sharedPref.edit()) {
                    putString("ImgUri",getImg)
                    apply()
                }
            }
        }

        Log.d("ee", "load preference!")
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val tmp = sharedPref?.getString("ImgUri","null")
        Log.d("ee","tmp : $tmp")

        if(tmp != null){
            val changeImg = Uri.parse(tmp)
            binding.mProfileImg.setImageURI(changeImg)
        }

        binding.MPSIPBtn.setOnClickListener {
            //내가 만든 설문지 화면으로 넘어감
//            val userid = auth!!.currentUser?.uid
//            val reference = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("name")
//            Log.d("ITM","아임 수아수 $reference")

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