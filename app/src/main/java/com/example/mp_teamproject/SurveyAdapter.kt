package com.example.mp_teamproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.activity_create_survey.view.*


//data를 담을 layout 파일
//class SurveyAdapter (list: ArrayList<SurveyData>) : RecyclerView.Adapter<CustomViewHolder>() {
//    var mList : ArrayList<SurveyData> = list
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_create_survey,parent,false)
//        return CustomViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return mList.size
//    }
//
//    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//        val p = mList.get(position)
//        holder.setHolder(p)
//    }
//
//}
//
//class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    fun setHolder(surveyData: SurveyData) {
//        itemView.titleText.text = surveyData.title
//        itemView.writerText.text = surveyData.writer
//        itemView.InstitutionText.text = surveyData.institution
//        itemView.purposeText.text = surveyData.purpose
//        itemView.categorySpinner.p2 = surveyData.category // p2 -> Int type
//        itemView.startDate.text = surveyData.startDate
//        itemView.endDate.text = surveyDate.endDate
//        itemView.tv_phone.text = surveyData.surveyorInfo
//        itemView.tv_phone.text = surveyData.surveyContent
//        itemView.tv_phone.text = surveyData.uri
//
//    }
//}