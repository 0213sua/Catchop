package com.example.mp_teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mp_teamproject.databinding.ActivityMainBinding

class Main : AppCompatActivity() {
    val homeFragment = HomeFragment()
    val mytreeFragment = MytreeFragment()
    val categoryFragment = CategoryFragment()
    val myPageFragment = MypageFragment()
    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, homeFragment).commit()

        if(intent.hasExtra("back")){
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragments_container,categoryFragment)
                commit()
            }
        }

        binding.navigationview.setOnItemSelectedListener {
            //change fragment
            MenuItem -> when(MenuItem.itemId){
            R.id.home_menu -> changeFragment(homeFragment)
            R.id.mytree_menu -> changeFragment(mytreeFragment)
            R.id.category_menu -> changeFragment(categoryFragment)
            R.id.mypage_menu -> changeFragment(myPageFragment)
        }
            true
        }
        //init fragment


    }
    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragments_container,fragment)
            commit()
        }
    }

}