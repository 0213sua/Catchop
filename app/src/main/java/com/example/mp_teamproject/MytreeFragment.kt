package com.example.mp_teamproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mp_teamproject.databinding.FragmentHomeBinding
import com.example.mp_teamproject.databinding.FragmentMytreeBinding

class MytreeFragment : Fragment() {

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
        var num_of_survey:Int = 1

        val binding = FragmentMytreeBinding.inflate(inflater,container,false)
        if(num_of_survey < 10){
//            binding.imageview1.setImageResource(R.drawable.mini_tree)
        }else{
            binding.imageView.setImageResource(R.drawable.tree)
        }

        return binding.root
    }

    companion object {
        fun newInstance():MytreeFragment{
            return MytreeFragment()
        }
    }
}