package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profiles")
data class ChildProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val ageYears: Int,
    val developmentalStage: String,
    val targetPhonemesCsv: String,
    val clinicalGoal: String,
    val totalStars: Int = 0,
    val completedExercisesCount: Int = 0,
    val streakDays: Int = 1,
    val isSelected: Boolean = false
)

@Entity(tableName = "exercise_progress")
data class ExerciseProgressEntity(
    @PrimaryKey val exerciseId: String,
    val childId: Int,
    val phonemeLetter: String,
    val starsEarned: Int,
    val attemptsCount: Int,
    val bestAccuracyPercent: Int,
    val lastPracticedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "practice_logs")
data class PracticeLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val childId: Int,
    val category: String, // "مكتبة النطق", "الجمباز الفموي", "التنفس والنفخ", "التمييز السمعي"
    val phonemeLetter: String,
    val exerciseTitle: String,
    val difficultyLabel: String,
    val developmentalStageLabel: String,
    val starsEarned: Int,
    val accuracyPercent: Int,
    val therapistNote: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_exercises")
data class CustomExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phonemeLetter: String,
    val title: String,
    val targetWordOrPhrase: String,
    val syllableBreakdown: String,
    val positionLabel: String, // "أول الكلمة", "وسط الكلمة", "آخر الكلمة", "جملة"
    val difficultyCode: String, // "BEGINNER", "INTERMEDIATE", "ADVANCED"
    val developmentalStageCode: String, // "STAGE_EARLY", "STAGE_MIDDLE", "STAGE_LATE"
    val articulationTip: String,
    val starsEarned: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
