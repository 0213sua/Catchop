package com.example.mp_teamproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp_teamproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    //adapter

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
        val binding = FragmentHomeBinding.inflate(inflater,container,false)

        /**

        val data = mutableListOf<String>()
        for(i in 1..9){
            data.add("Item $i")
        }
        val adapter = MyAdapter(data)
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter
         *
         */
        return binding.root

    }

    companion object {
        fun newInstance():HomeFragment{
            return HomeFragment()
        }
    }
}