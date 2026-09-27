package com.example.data.local

import kotlinx.coroutines.flow.Flow

class OrthophonyRepository(private val dao: OrthophonyDao) {

    val allProfiles: Flow<List<ChildProfileEntity>> = dao.getAllChildProfiles()
    val selectedProfile: Flow<ChildProfileEntity?> = dao.getSelectedChildProfile()
    val allExerciseProgress: Flow<List<ExerciseProgressEntity>> = dao.getAllExerciseProgress()
    val allPracticeLogs: Flow<List<PracticeLogEntity>> = dao.getAllPracticeLogs()
    val allCustomExercises: Flow<List<CustomExerciseEntity>> = dao.getAllCustomExercises()

    suspend fun ensureDefaultDataSeeded() {
        if (dao.getChildProfileCount() == 0) {
            val child1Id = dao.insertChildProfile(
                ChildProfileEntity(
                    name = "ياسين",
                    ageYears = 5,
                    developmentalStage = "المرحلة المتوسطة (3.5 - 5 سنوات)",
                    targetPhonemesCsv = "س,ش,ر,ل",
                    clinicalGoal = "تحسين مخارج حروف الصفير (س، ش) وتدريب اللسان على حرف الراء (ر) واللام (ل)",
                    totalStars = 18,
                    completedExercisesCount = 6,
                    streakDays = 4,
                    isSelected = true
                )
            ).toInt()

            dao.insertChildProfile(
                ChildProfileEntity(
                    name = "مريم",
                    ageYears = 3,
                    developmentalStage = "المرحلة المبكرة (2 - 3.5 سنوات)",
                    targetPhonemesCsv = "ب,ف,ل,ك",
                    clinicalGoal = "تنمية الحصيلة اللغوية المبكرة وإتقان الأصوات الشفوية واللام",
                    totalStars = 12,
                    completedExercisesCount = 4,
                    streakDays = 2,
                    isSelected = false
                )
            )

            // Seed initial progress & realistic session logs so the progress screen is immediately informative
            dao.upsertExerciseProgress(
                ExerciseProgressEntity(
                    exerciseId = "seen_beg_1",
                    childId = child1Id,
                    phonemeLetter = "س",
                    starsEarned = 3,
                    attemptsCount = 3,
                    bestAccuracyPercent = 95
                )
            )
            dao.upsertExerciseProgress(
                ExerciseProgressEntity(
                    exerciseId = "sheen_beg_1",
                    childId = child1Id,
                    phonemeLetter = "ش",
                    starsEarned = 3,
                    attemptsCount = 2,
                    bestAccuracyPercent = 90
                )
            )
            dao.upsertExerciseProgress(
                ExerciseProgressEntity(
                    exerciseId = "laam_beg_1",
                    childId = child1Id,
                    phonemeLetter = "ل",
                    starsEarned = 3,
                    attemptsCount = 2,
                    bestAccuracyPercent = 92
                )
            )
            dao.upsertExerciseProgress(
                ExerciseProgressEntity(
                    exerciseId = "raa_beg_1",
                    childId = child1Id,
                    phonemeLetter = "ر",
                    starsEarned = 2,
                    attemptsCount = 4,
                    bestAccuracyPercent = 78
                )
            )

            dao.insertPracticeLog(
                PracticeLogEntity(
                    childId = child1Id,
                    category = "مكتبة النطق",
                    phonemeLetter = "س",
                    exerciseTitle = "قطار الصفير والمقاطع (سَ - سُ - سِ)",
                    difficultyLabel = "مبتدئ",
                    developmentalStageLabel = "3.5 - 5 سنوات",
                    starsEarned = 3,
                    accuracyPercent = 95,
                    therapistNote = "إغلاق الأسنان ممتاز وعدم خروج اللسان بين الأسنان."
                )
            )
            dao.insertPracticeLog(
                PracticeLogEntity(
                    childId = child1Id,
                    category = "مكتبة النطق",
                    phonemeLetter = "ر",
                    exerciseTitle = "نقر طرف اللسان والمقاطع (رَ - رُ - رِ)",
                    difficultyLabel = "مبتدئ",
                    developmentalStageLabel = "5 - 7 سنوات",
                    starsEarned = 2,
                    accuracyPercent = 78,
                    therapistNote = "بداية ظهور الاهتزاز التكراري مع حرف الدال المساعد (دْرَ)."
                )
            )
        }
    }

    suspend fun selectProfile(childId: Int) {
        dao.selectChildProfile(childId)
    }

    suspend fun addChildProfile(
        name: String,
        ageYears: Int,
        developmentalStage: String,
        targetPhonemesCsv: String,
        clinicalGoal: String
    ) {
        val newId = dao.insertChildProfile(
            ChildProfileEntity(
                name = name,
                ageYears = ageYears,
                developmentalStage = developmentalStage,
                targetPhonemesCsv = targetPhonemesCsv,
                clinicalGoal = clinicalGoal,
                totalStars = 0,
                completedExercisesCount = 0,
                streakDays = 1,
                isSelected = true
            )
        ).toInt()
        dao.selectChildProfile(newId)
    }

    suspend fun recordExerciseCompletion(
        childId: Int,
        exerciseId: String,
        category: String,
        phonemeLetter: String,
        exerciseTitle: String,
        difficultyLabel: String,
        developmentalStageLabel: String,
        starsEarned: Int,
        accuracyPercent: Int,
        therapistNote: String
    ) {
        dao.upsertExerciseProgress(
            ExerciseProgressEntity(
                exerciseId = exerciseId,
                childId = childId,
                phonemeLetter = phonemeLetter,
                starsEarned = starsEarned,
                attemptsCount = 1,
                bestAccuracyPercent = accuracyPercent
            )
        )
        dao.insertPracticeLog(
            PracticeLogEntity(
                childId = childId,
                category = category,
                phonemeLetter = phonemeLetter,
                exerciseTitle = exerciseTitle,
                difficultyLabel = difficultyLabel,
                developmentalStageLabel = developmentalStageLabel,
                starsEarned = starsEarned,
                accuracyPercent = accuracyPercent,
                therapistNote = therapistNote
            )
        )
        dao.addStarsToChild(childId, starsEarned)
    }

    suspend fun addCustomExercise(exercise: CustomExerciseEntity) {
        dao.insertCustomExercise(exercise)
    }

    suspend fun updateCustomExerciseStars(id: Int, stars: Int) {
        dao.updateCustomExerciseStars(id, stars)
    }

    suspend fun deleteCustomExercise(id: Int) {
        dao.deleteCustomExercise(id)
    }
}
