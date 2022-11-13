package com.example.mp_teamproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mp_teamproject.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {
    lateinit var binding: FragmentCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater)

        binding.imgv1.setOnClickListener {
            val intent = Intent(this@CategoryFragment.requireContext(),SelectedCategory::class.java)
            startActivity(intent)
        }

        return binding.root

    }

    companion object {
        fun newInstance():CategoryFragment{
            return CategoryFragment()
        }
    }
}