package com.example.mp_teamproject

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mp_teamproject.databinding.ActivityEditProfileBinding
import java.text.SimpleDateFormat
import com.example.mp_teamproject.MypageFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.*


class EditProfile : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    val binding by lazy{ ActivityEditProfileBinding.inflate(layoutInflater)}
    // Permisisons
    val PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val PERMISSIONS_REQUEST = 100

    // Request Code
    private val BUTTON2 = 200 // 내부 저장소에 저장하기

    var imgUri : Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //permission check and request
        checkPermissions(PERMISSIONS,PERMISSIONS_REQUEST)

        auth = FirebaseAuth.getInstance()
        val userid = auth!!.currentUser?.uid
        val name_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("username")
        val sex_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("sex")
        val birth_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("birth")
        val email_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("email")
        val pw_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("pw")
        val phone_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("phone")
        val job_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("job")


        name_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val name = dataSnapshot.getValue()
                binding.EPNameEditText.setText(name.toString())
                Log.d("ITM", "Value is: $name")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ITM", "Failed to read value.", error.toException())
            }
        })

        sex_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val sex = dataSnapshot.getValue()
                binding.EPSexEditText.setText(sex.toString())
                Log.d("ITM", "Value is: $sex")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ITM", "Failed to read value.", error.toException())
            }
        })

        birth_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val birth = dataSnapshot.getValue()
                binding.EPBirthEditText.setText(birth.toString())
                Log.d("ITM", "Value is: $birth")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ITM", "Failed to read value.", error.toException())
            }
        })

        email_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val email = dataSnapshot.getValue()
                binding.EPEmailEditText.setText(email.toString())
                Log.d("ITM", "Value is: $email")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ITM", "Failed to read value.", error.toException())
            }
        })

        pw_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val pw = dataSnapshot.getValue()
                binding.EPPasswordEditText.setText(pw.toString())
                Log.d("ITM", "Value is: $pw")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ITM", "Failed to read value.", error.toException())
            }
        })
        phone_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val phone = dataSnapshot.getValue()
                binding.EPPhoneEditText.setText(phone.toString())
                Log.d("ITM", "Value is: $phone")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ITM", "Failed to read value.", error.toException())
            }
        })

        job_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val job = dataSnapshot.getValue()
                binding.EPJobEditText.setText(job.toString())
                Log.d("ITM", "Value is: $job")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ITM", "Failed to read value.", error.toException())
            }
        })

        // load preference
        Log.d("ee", "load preference!")
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val tmp = sharedPref?.getString("ImgUri","null")
        Log.d("ee","tmp : $tmp")
        if(tmp != null){
            val changeImg = Uri.parse(tmp)
            binding.profileImg.setImageURI(changeImg)
        }

        binding.profileImg.setOnClickListener{

            //resolve activity : check the calling target app exists on the device
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, BUTTON2)
            }

        }
        binding.SignupBtn.setOnClickListener{
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("ImgUri",imgUri.toString())
                apply()
            }
            Toast.makeText(this,"preference saved",Toast.LENGTH_SHORT).show()
            Log.d("ee","save preference : ${imgUri.toString()}")

            val intent = Intent(this, Main::class.java)
            intent.putExtra("myprofile",imgUri.toString())
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode) {
                // store in : data/data/com.example.mp_teamproject/files/image
                BUTTON2 -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    saveBitmapAsJPGFile(imageBitmap)
                    binding.profileImg.setImageBitmap(imageBitmap)

                }
            }
        }
    }

    private fun newJpgFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())

        return "${filename}.jpg"
    }
    //Bitmap data store with bitmap file
    private fun saveBitmapAsJPGFile(bitmap: Bitmap) {
        val path = File(filesDir, "image")
        if(!path.exists()){
            path.mkdirs()
        }
        Log.d("dd","file path : $path")
        val file = File(path, newJpgFileName())
        Log.d("dd","file path2 : $file")
        var imageFile: OutputStream? = null

        try{
            file.createNewFile()
            imageFile = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageFile)
            imageFile.close()
            imgUri = Uri.parse(file.absolutePath)
            Toast.makeText(this, file.absolutePath, Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            null
        }
    }

    //check permission : ContextCompat.CheckSelfPermission()
    //request permission : ActivityCompat.requestPermission()
    private fun checkPermissions(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList : MutableList<String> = mutableListOf()
        for(permission in permissions){
            val result = ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission)
            }
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), PERMISSIONS_REQUEST)
            return false
        }
        return true
    }
    //check result of permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Please approve the authority :)", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}