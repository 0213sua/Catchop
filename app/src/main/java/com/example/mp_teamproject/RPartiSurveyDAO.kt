package com.example.mp_teamproject

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RPartiSurveyDAO {
    @Insert
    fun insertSurveyID(partiSurveyID:RPartiSurvey)

    @Update
    fun updateSurveyID(partiSurveyID:RPartiSurvey)

    @Delete
    fun deleteSurveyID(partiSurveyID:RPartiSurvey)

    @Query("SELECT * FROM RPartiSurvey")
    fun getAll():List<RPartiSurvey>
}