package com.example.mp_teamproject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RPartiSurvey {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var surveyID = ""

}