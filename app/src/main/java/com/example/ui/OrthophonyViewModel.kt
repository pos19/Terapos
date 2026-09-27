package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.OrthophonyAudioEngine
import com.example.data.curriculum.DevelopmentalStage
import com.example.data.curriculum.DifficultyLevel
import com.example.data.curriculum.InteractiveExercise
import com.example.data.curriculum.InteractivePromptItem
import com.example.data.curriculum.MinimalPairChallenge
import com.example.data.curriculum.OralMotorExercise
import com.example.data.curriculum.OrthophonyCurriculum
import com.example.data.curriculum.PhonemeInfo
import com.example.data.local.ChildProfileEntity
import com.example.data.local.CustomExerciseEntity
import com.example.data.local.ExerciseProgressEntity
import com.example.data.local.OrthophonyDatabase
import com.example.data.local.OrthophonyRepository
import com.example.data.local.PracticeLogEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppDestination(val route: String, val labelArabic: String) {
    LIBRARY("library", "مكتبة النطق"),
    ORAL_BREATHING("oral_breathing", "الجمباز والتنفس"),
    AUDITORY_AAC("auditory_aac", "السمع والتواصل"),
    CHILD_PROFILE("child_profile", "ملف الطفل")
}

class OrthophonyViewModel(application: Application) : AndroidViewModel(application) {

    private val database = OrthophonyDatabase.getInstance(application)
    private val repository = OrthophonyRepository(database.orthophonyDao())
    val audioEngine = OrthophonyAudioEngine(application, viewModelScope)

    // Navigation state
    private val _currentDestination = MutableStateFlow(AppDestination.LIBRARY)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    // Library Filter States
    private val _selectedPhonemeFilter = MutableStateFlow<String?>(null) // null = الكل
    val selectedPhonemeFilter: StateFlow<String?> = _selectedPhonemeFilter.asStateFlow()

    private val _selectedDifficultyFilter = MutableStateFlow<DifficultyLevel?>(null) // null = الكل
    val selectedDifficultyFilter: StateFlow<DifficultyLevel?> = _selectedDifficultyFilter.asStateFlow()

    private val _selectedStageFilter = MutableStateFlow<DevelopmentalStage?>(null) // null = الكل
    val selectedStageFilter: StateFlow<DevelopmentalStage?> = _selectedStageFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Currently active interactive exercise in the player
    private val _activeExercise = MutableStateFlow<InteractiveExercise?>(null)
    val activeExercise: StateFlow<InteractiveExercise?> = _activeExercise.asStateFlow()

    // Database Flows
    val allChildProfiles: StateFlow<List<ChildProfileEntity>> = repository.allProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedChildProfile: StateFlow<ChildProfileEntity?> = repository.selectedProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allExerciseProgress: StateFlow<List<ExerciseProgressEntity>> = repository.allExerciseProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPracticeLogs: StateFlow<List<PracticeLogEntity>> = repository.allPracticeLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCustomExercises: StateFlow<List<CustomExerciseEntity>> = repository.allCustomExercises
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combined & Filtered Exercises (Built-in + User Custom Exercises converted to InteractiveExercise)
    val filteredExercises: StateFlow<List<InteractiveExercise>> = combine(
        _selectedPhonemeFilter,
        _selectedDifficultyFilter,
        _selectedStageFilter,
        _searchQuery,
        allCustomExercises
    ) { phonemeFilter, difficultyFilter, stageFilter, query, customList ->
        val convertedCustom = customList.map { custom ->
            val matchedPhoneme = OrthophonyCurriculum.phonemeCatalog.find { it.letter == custom.phonemeLetter }
                ?: OrthophonyCurriculum.phonemeCatalog.first()
            val diff = DifficultyLevel.entries.find { it.name == custom.difficultyCode } ?: DifficultyLevel.BEGINNER
            val stage = DevelopmentalStage.entries.find { it.name == custom.developmentalStageCode } ?: DevelopmentalStage.STAGE_MIDDLE

            InteractiveExercise(
                id = "custom_${custom.id}",
                phonemeLetter = custom.phonemeLetter,
                phonemeName = matchedPhoneme.nameArabic,
                title = custom.title,
                subtitle = "تمرين مخصص • ${custom.positionLabel}",
                difficulty = diff,
                developmentalStage = stage,
                animationTheme = matchedPhoneme.animationTheme,
                tonguePlacementType = matchedPhoneme.tonguePlacementType,
                clinicalGoal = "تمرين علاجي مخصص أضيف لخطة الطفل التدريبية.",
                therapistTip = custom.articulationTip.ifBlank { matchedPhoneme.commonErrorCorrection },
                items = listOf(
                    InteractivePromptItem(
                        textArabic = custom.targetWordOrPhrase,
                        syllabicBreakdown = custom.syllableBreakdown.ifBlank { custom.targetWordOrPhrase },
                        positionTag = custom.positionLabel,
                        visualCueLabel = custom.title,
                        helperTip = custom.articulationTip.ifBlank { "انطق المقاطع بوضوح وتأنٍّ" }
                    )
                )
            )
        }

        val combined = convertedCustom + OrthophonyCurriculum.interactiveExercises
        combined.filter { ex ->
            val matchesPhoneme = phonemeFilter == null || ex.phonemeLetter == phonemeFilter
            val matchesDifficulty = difficultyFilter == null || ex.difficulty == difficultyFilter
            val matchesStage = stageFilter == null || ex.developmentalStage == stageFilter
            val matchesSearch = query.isBlank() ||
                ex.title.contains(query, ignoreCase = true) ||
                ex.subtitle.contains(query, ignoreCase = true) ||
                ex.phonemeName.contains(query, ignoreCase = true) ||
                ex.items.any { it.textArabic.contains(query, ignoreCase = true) }

            matchesPhoneme && matchesDifficulty && matchesStage && matchesSearch
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        OrthophonyCurriculum.interactiveExercises
    )

    val phonemeCatalog: List<PhonemeInfo> = OrthophonyCurriculum.phonemeCatalog
    val oralMotorExercises: List<OralMotorExercise> = OrthophonyCurriculum.oralMotorExercises
    val minimalPairChallenges: List<MinimalPairChallenge> = OrthophonyCurriculum.minimalPairChallenges
    val aacCards = OrthophonyCurriculum.aacCommunicationCards

    init {
        viewModelScope.launch {
            repository.ensureDefaultDataSeeded()
        }
    }

    fun navigateTo(destination: AppDestination) {
        _currentDestination.value = destination
    }

    fun setPhonemeFilter(phoneme: String?) {
        _selectedPhonemeFilter.value = phoneme
        if (phoneme != null) {
            audioEngine.playPhonemeCueSound(phoneme)
        }
    }

    fun setDifficultyFilter(difficulty: DifficultyLevel?) {
        _selectedDifficultyFilter.value = difficulty
    }

    fun setStageFilter(stage: DevelopmentalStage?) {
        _selectedStageFilter.value = stage
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearAllFilters() {
        _selectedPhonemeFilter.value = null
        _selectedDifficultyFilter.value = null
        _selectedStageFilter.value = null
        _searchQuery.value = ""
    }

    fun openExercisePlayer(exercise: InteractiveExercise) {
        _activeExercise.value = exercise
        audioEngine.playPhonemeCueSound(exercise.phonemeLetter)
    }

    fun closeExercisePlayer() {
        audioEngine.stopMicrophoneRecording()
        _activeExercise.value = null
    }

    fun speakText(text: String, slow: Boolean = false, phonemeHint: String = "") {
        audioEngine.speakArabic(text, slowSyllabic = slow, phonemeHint = phonemeHint)
    }

    fun completeExerciseWithStars(
        exercise: InteractiveExercise,
        starsEarned: Int,
        accuracyPercent: Int,
        therapistNote: String
    ) {
        audioEngine.playStarRewardSound(starsEarned)
        viewModelScope.launch {
            val childId = selectedChildProfile.value?.id ?: 1
            repository.recordExerciseCompletion(
                childId = childId,
                exerciseId = exercise.id,
                category = "مكتبة النطق",
                phonemeLetter = exercise.phonemeLetter,
                exerciseTitle = exercise.title,
                difficultyLabel = exercise.difficulty.labelArabic,
                developmentalStageLabel = exercise.developmentalStage.shortBadge,
                starsEarned = starsEarned,
                accuracyPercent = accuracyPercent,
                therapistNote = therapistNote
            )
        }
    }

    fun logActivityCompletion(
        category: String,
        phonemeLetter: String,
        title: String,
        difficultyLabel: String,
        stageLabel: String,
        stars: Int,
        accuracy: Int,
        note: String
    ) {
        audioEngine.playStarRewardSound(stars)
        viewModelScope.launch {
            val childId = selectedChildProfile.value?.id ?: 1
            repository.recordExerciseCompletion(
                childId = childId,
                exerciseId = "${category}_${System.currentTimeMillis()}",
                category = category,
                phonemeLetter = phonemeLetter,
                exerciseTitle = title,
                difficultyLabel = difficultyLabel,
                developmentalStageLabel = stageLabel,
                starsEarned = stars,
                accuracyPercent = accuracy,
                therapistNote = note
            )
        }
    }

    fun addCustomExercise(
        phonemeLetter: String,
        title: String,
        targetWord: String,
        syllableBreakdown: String,
        positionLabel: String,
        difficulty: DifficultyLevel,
        stage: DevelopmentalStage,
        tip: String
    ) {
        viewModelScope.launch {
            repository.addCustomExercise(
                CustomExerciseEntity(
                    phonemeLetter = phonemeLetter,
                    title = title,
                    targetWordOrPhrase = targetWord,
                    syllableBreakdown = syllableBreakdown,
                    positionLabel = positionLabel,
                    difficultyCode = difficulty.name,
                    developmentalStageCode = stage.name,
                    articulationTip = tip
                )
            )
        }
    }

    fun deleteCustomExercise(customIdString: String) {
        val id = customIdString.removePrefix("custom_").toIntOrNull() ?: return
        viewModelScope.launch {
            repository.deleteCustomExercise(id)
        }
    }

    fun selectChildProfile(childId: Int) {
        viewModelScope.launch {
            repository.selectProfile(childId)
        }
    }

    fun addNewChildProfile(
        name: String,
        ageYears: Int,
        stageLabel: String,
        targetPhonemes: String,
        goal: String
    ) {
        viewModelScope.launch {
            repository.addChildProfile(
                name = name,
                ageYears = ageYears,
                developmentalStage = stageLabel,
                targetPhonemesCsv = targetPhonemes,
                clinicalGoal = goal
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.shutdown()
    }
}
