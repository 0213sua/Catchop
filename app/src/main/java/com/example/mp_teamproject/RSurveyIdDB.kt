package com.example.mp_teamproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RPartiSurvey::class],
    version = 1,
    exportSchema = false
)
abstract class RSurveyIdDB: RoomDatabase() {
    abstract fun rDao() : RPartiSurveyDAO
    companion object {
        val databaseName = "survey_db"
        private var rSurveyIdDB: RSurveyIdDB? = null
        fun getInstance(context: Context) : RSurveyIdDB?{
            // if appDatabase is null, create object
            if(rSurveyIdDB==null){
                rSurveyIdDB = Room.databaseBuilder(context,RSurveyIdDB::class.java,databaseName)
                    .allowMainThreadQueries()
                    .build()
            }
            // if not null, return existing object
            return rSurveyIdDB
        }

    }

}

//@Volatile
//private var INSTANCE:RSurveyIdDB?=null
//fun getInstance(context: Context): RSurveyIdDB {
//// if the INSTANCE is not null, then return it,
//// if it is, then create the database
//    return INSTANCE ?: synchronized(this) {
//        val instance = Room.databaseBuilder(
//            context.applicationContext,
//            RSurveyIdDB::class.java,
//            "surveyID-database"
//        ).allowMainThreadQueries().build()
//        INSTANCE = instance
//// return instance
//        instance
//    }
//}

//@Synchronized
//fun getInstance(context: Context): RSurveyIdDB?{
//    if (instance == null){
//        instance = Room.databaseBuilder(
//            context.applicationContext,
//            RSurveyIdDB::class.java,
//            "survet-id.db"
//        )
//            .allowMainThreadQueries()
//            .build()
//    }
//    return instance
//}
