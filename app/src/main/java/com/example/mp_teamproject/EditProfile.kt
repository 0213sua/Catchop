package com.example.mp_teamproject

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
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
    private val BUTTON1 = 100 // 카메라 앱으로 사진찍고 imageView에 미리보기
    private val BUTTON2 = 200
    private val BUTTON3 = 300 // 카메라 앱으로 사진찍고 원본사진 내부 저장소에 저장하기
    private val BUTTON4 = 400 // 사진찍고 외부 저장소의 앱 디렉토리에 저장하기
    private val BUTTON5 = 500

    // 원본 사진이 저장되는 Uri
    private var photoUri: Uri? = null
    var imgUri : Uri? = null // 미리보기 사진 uri 저장



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //permission check and request
        checkPermissions(PERMISSIONS,PERMISSIONS_REQUEST)

        auth = FirebaseAuth.getInstance()
        val userid = auth!!.currentUser?.uid
        val name_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("username")
        val email_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("email")
        val pw_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("pw")
        val phone_ref = FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("phone")



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

        email_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val email = dataSnapshot.getValue()
                binding.EPNameEditText.setText(email.toString())
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
                binding.EPNameEditText.setText(pw.toString())
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
                binding.EPNameEditText.setText(phone.toString())
                Log.d("ITM", "Value is: $phone")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ITM", "Failed to read value.", error.toException())
            }
        })






        binding.profileImg.setOnClickListener{

            //resolve activity : implict intent 호출 대상앱이 기기에 존재하는지 확인
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(takePictureIntent, BUTTON1)
                startActivityForResult(takePictureIntent, BUTTON2)
            }

        }
        binding.SignupBtn.setOnClickListener{
            val intent = Intent(this, Main::class.java)
            Log.d("ee","imgUri in edit profile: $imgUri")
            intent.putExtra("imgUri",imgUri.toString())
            startActivity(intent)
        }

//        loadFile(profileImg)

    }

    fun loadFile(view: View) {
        val filename = "image"
        val file = File(filesDir, filename)
        val files: Array<String> = fileList()
        for (i in files)
            Log.d("ee", i)
        val reader = BufferedReader(FileReader(file))
        reader.readLines().forEach {
            Log.d("ee", it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode) {
                BUTTON1 -> {
//                    미리보기 사진 intent.extra.get("data")에 저장
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.profileImg.setImageBitmap(imageBitmap)
                }
                // 미리보기 사진 내부 저장소에 저장
                // 내부저장소 위치 : data/data/com.example.mp_teamproject/files/image
                BUTTON2 -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    saveBitmapAsJPGFile(imageBitmap)
                    binding.profileImg.setImageBitmap(imageBitmap)

                }

                BUTTON3 -> {

                }
            }
        }
    }

    private fun newJpgFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())

        return "${filename}.jpg"
    }
    //Bitmap 데이터를 bitmap file로 저장, 경로 = 내부저장소
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

    //권한 확인하기 : ContextCompat.CheckSelfPermission()
    //권한 요청하기 : ActivityCompat.requestPermission()
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
    //권한요청 결과 확인
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한 승인 부탁드립니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}