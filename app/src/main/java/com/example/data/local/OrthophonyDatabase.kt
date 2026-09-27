package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChildProfileEntity::class,
        ExerciseProgressEntity::class,
        PracticeLogEntity::class,
        CustomExerciseEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class OrthophonyDatabase : RoomDatabase() {
    abstract fun orthophonyDao(): OrthophonyDao

    companion object {
        @Volatile
        private var INSTANCE: OrthophonyDatabase? = null

        fun getInstance(context: Context): OrthophonyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrthophonyDatabase::class.java,
                    "nutqi_orthophony_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
