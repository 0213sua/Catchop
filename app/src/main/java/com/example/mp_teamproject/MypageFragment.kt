package com.example.mp_teamproject

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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

        binding.MPEditprofileBtn.setOnClickListener {
            val intent = Intent(this@MypageFragment.requireContext(),EditProfile::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }

        binding.MPLogoutBtn.setOnClickListener {

            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("LOG OUT")
                .setMessage("Do you want logout from Catchop?")
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

        binding.MPDeleteABtn.setOnClickListener {
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Membership Withdrawal")
                .setMessage("Do you want withdraw from Catchop?")
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