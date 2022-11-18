package com.example.mp_teamproject

// database에 올릴 data class
// radio button으로 입력받는 정보는 어떻게 해야하지..? 그것도 var ~:String? 해야하나?
//data class SurveyData (var title : String?, var writer: String?, var institution: String?, var purpose: String?, var category:Int?, var startDate:String?, var endDate:String?, var surveyorInfo:String?, var surveyContent:String?, var uri:String){}
class SurveyData{
    //설문지 id
    var surveyId = ""

    //설문지 작성자 id == registerBtn을 누른 사람
    var writerId = ""

    //설문지 참여자 id == submitBtn을 누른 사람
    var participantId = ""

    //
    var title = ""
    var writer = ""
    var institution =""
    var purpose =""
    var category = ""
    var startDate =""
    var endDate=""
    var surveyorInfo =""
    var surveyContent =""
    var uri =""
}