package com.example.mp_teamproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mp_teamproject.databinding.FragmentCategoryBinding
import com.example.mp_teamproject.databinding.FragmentHomeBinding
import com.example.mp_teamproject.databinding.ItemMainBinding

class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
// 항목 레이아웃 xml 파일에 해당하는 바인딩 객체만 가지고 있으면 됨

class MyAdapter(val datas: MutableList<String>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    // 뷰 홀더 준비 위해 자동 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder = MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent, false))

    // 각 항목 구성 위해 호출
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        // 뷰에 데이터 출력
        binding.itemData2.text = datas[position]

        // 뷰에 이벤트 추가
        binding.itemRoot.setOnClickListener {

        }
    }
    // 항목 개수 판단 위해 자동 호출
    override fun getItemCount(): Int = datas.size

}

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
        val datas = mutableListOf<String>()
        for(i in 1..10){
            datas.add("Item $i")
        }

        //리사이클러 뷰에 LayoutManager, Adapter, (ItemDecoration 적용)
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView1.layoutManager = layoutManager
        val adapter = MyAdapter(datas)
        binding.recyclerView1.adapter = adapter

        binding.floatingBtn1.setOnClickListener {
            val intent = Intent(this@HomeFragment.requireContext(),CreateSurvey::class.java)
            startActivity(intent)
        }
        return binding.root

    }

    companion object {
        fun newInstance():HomeFragment{
            return HomeFragment()
        }
    }
}