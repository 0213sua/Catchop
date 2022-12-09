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
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.*


class EditProfile : AppCompatActivity() {
    val binding by lazy{ ActivityEditProfileBinding.inflate(layoutInflater)}
    // Permisisons
    val PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val PERMISSIONS_REQUEST = 100

    // Request Code
//    private val BUTTON1 = 100 // 카메라 앱으로 사진찍고 imageView에 미리보기
    private val BUTTON2 = 200 // 내부 저장소에 저장하기

    // 원본 사진이 저장되는 Uri
    private var photoUri: Uri? = null
    var imgUri : Uri? = null // 미리보기 사진 uri 저장


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //permission check and request
        checkPermissions(PERMISSIONS,PERMISSIONS_REQUEST)

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

            //resolve activity : implict intent 호출 대상앱이 기기에 존재하는지 확인
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(takePictureIntent, BUTTON1)
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
                // 미리보기 사진 내부 저장소에 저장
                // 내부저장소 위치 : data/data/com.example.mp_teamproject/files/image
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