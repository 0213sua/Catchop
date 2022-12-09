package com.example.mp_teamproject

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RPartiSurveyDAO {
    @Insert
    fun insert(sid:RPartiSurvey)

    @Update
    fun update(sid:RPartiSurvey)

    @Delete
    fun delete(sid:RPartiSurvey)

    @Query("SELECT * FROM tb_survey")
    fun getAll():List<RPartiSurvey>
}