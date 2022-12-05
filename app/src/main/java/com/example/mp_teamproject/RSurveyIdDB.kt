package com.example.mp_teamproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RPartiSurvey::class],
    version = 1
)
abstract class RSurveyIdDB: RoomDatabase() {
    abstract fun rPartiSurveyDao() : RPartiSurveyDAO
    companion object {
        @Volatile
        private var instance:RSurveyIdDB?=null

        fun getInstance(context: Context): RSurveyIdDB? {
            if (instance == null) {
                synchronized(RSurveyIdDB::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RSurveyIdDB::class.java,
                        "surveyID-database"
                    )
                        .build()
                }
            }
            return instance
        }
    }

}