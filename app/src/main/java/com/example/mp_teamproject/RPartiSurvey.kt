package com.example.mp_teamproject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_survey")
data class RPartiSurvey(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "surveyID") var surveyID: String
){
    constructor(): this(null,"")
}
