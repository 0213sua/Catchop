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
            intent.putExtra("category_name1", "Administration")
            startActivity(intent)
        }
        binding.imgv2.setOnClickListener {
            val intent = Intent(this@CategoryFragment.requireContext(),SelectedCategory::class.java)
            intent.putExtra("category_name1", "Technology")
            startActivity(intent)
        }
        binding.imgv3.setOnClickListener {
            val intent = Intent(this@CategoryFragment.requireContext(),SelectedCategory::class.java)
            intent.putExtra("category_name1", "Education")
            startActivity(intent)
        }
        binding.imgv4.setOnClickListener {
            val intent = Intent(this@CategoryFragment.requireContext(),SelectedCategory::class.java)
            intent.putExtra("category_name1", "Traffic")
            startActivity(intent)
        }
        binding.imgv5.setOnClickListener {
            val intent = Intent(this@CategoryFragment.requireContext(),SelectedCategory::class.java)
            intent.putExtra("category_name1", "Land managing")
            startActivity(intent)
        }
        binding.imgv6.setOnClickListener {
            val intent = Intent(this@CategoryFragment.requireContext(),SelectedCategory::class.java)
            intent.putExtra("category_name1", "Agri-Animal-Fish")
            startActivity(intent)
        }
        binding.imgv7.setOnClickListener {
            val intent = Intent(this@CategoryFragment.requireContext(),SelectedCategory::class.java)
            intent.putExtra("category_name1", "Culture & Tourism")
            startActivity(intent)
        }
        binding.imgv8.setOnClickListener {
            val intent = Intent(this@CategoryFragment.requireContext(),SelectedCategory::class.java)
            intent.putExtra("category_name1", "Health & Medical")
            startActivity(intent)
        }
        binding.imgv9.setOnClickListener {
            val intent = Intent(this@CategoryFragment.requireContext(),SelectedCategory::class.java)
            intent.putExtra("category_name1", "Food")
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