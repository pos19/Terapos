package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OrthophonyDao {

    // Child Profiles
    @Query("SELECT * FROM child_profiles ORDER BY isSelected DESC, id ASC")
    fun getAllChildProfiles(): Flow<List<ChildProfileEntity>>

    @Query("SELECT * FROM child_profiles WHERE isSelected = 1 LIMIT 1")
    fun getSelectedChildProfile(): Flow<ChildProfileEntity?>

    @Query("SELECT COUNT(*) FROM child_profiles")
    suspend fun getChildProfileCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChildProfile(profile: ChildProfileEntity): Long

    @Update
    suspend fun updateChildProfile(profile: ChildProfileEntity)

    @Query("UPDATE child_profiles SET isSelected = CASE WHEN id = :childId THEN 1 ELSE 0 END")
    suspend fun selectChildProfile(childId: Int)

    @Query("UPDATE child_profiles SET totalStars = totalStars + :addedStars, completedExercisesCount = completedExercisesCount + 1 WHERE id = :childId")
    suspend fun addStarsToChild(childId: Int, addedStars: Int)

    // Exercise Progress
    @Query("SELECT * FROM exercise_progress WHERE childId = :childId")
    fun getExerciseProgressForChild(childId: Int): Flow<List<ExerciseProgressEntity>>

    @Query("SELECT * FROM exercise_progress")
    fun getAllExerciseProgress(): Flow<List<ExerciseProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExerciseProgress(progress: ExerciseProgressEntity)

    // Practice Logs
    @Query("SELECT * FROM practice_logs ORDER BY timestamp DESC")
    fun getAllPracticeLogs(): Flow<List<PracticeLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPracticeLog(log: PracticeLogEntity)

    @Query("DELETE FROM practice_logs WHERE id = :logId")
    suspend fun deletePracticeLog(logId: Int)

    // Custom Exercises
    @Query("SELECT * FROM custom_exercises ORDER BY createdAt DESC")
    fun getAllCustomExercises(): Flow<List<CustomExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomExercise(exercise: CustomExerciseEntity)

    @Query("UPDATE custom_exercises SET starsEarned = :stars WHERE id = :id")
    suspend fun updateCustomExerciseStars(id: Int, stars: Int)

    @Query("DELETE FROM custom_exercises WHERE id = :id")
    suspend fun deleteCustomExercise(id: Int)
}
